r"""
Step 2: Scrape every class's title + YouTube URL.

Uses the session cookies saved by capture.py (no browser needed) to fetch each
class detail page, extract the embedded YouTube video, and export to
classes.xlsx, classes.csv and classes.docx.

Run:  .venv\Scripts\python.exe scrape.py
"""
import json
import re
import sys
import time
from pathlib import Path

import requests

# console may be cp1252; class titles contain unicode (→, &, etc.)
try:
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")
except Exception:
    pass

HERE = Path(__file__).parent
COOKIES = HERE / "cookies.json"
LINKS_JSON = HERE / "page_links.json"

BASE = "https://www.algouniversity.com"
HEADERS = {"User-Agent": "Mozilla/5.0"}
CLASS_RE = re.compile(r"/class/(\d+)/?$")          # detail links, no ?tab=
EMBED_RE = re.compile(
    r"(?:youtube\.com/embed/|youtu\.be/embed/|youtu\.be/|youtube\.com/watch\?v=)"
    r"([A-Za-z0-9_-]{6,})")


def load_session():
    cks = json.loads(COOKIES.read_text(encoding="utf-8"))
    s = requests.Session()
    for c in cks:
        s.cookies.set(c["name"], c["value"], domain=c.get("domain"))
    s.headers.update(HEADERS)
    return s


def load_classes():
    """Return ordered [(id, title)] from the captured listing page."""
    data = json.loads(LINKS_JSON.read_text(encoding="utf-8"))
    seen, out = set(), []
    for a in data["anchors"]:
        m = CLASS_RE.search(a["href"])
        title = a["text"].strip()
        if not m or not title:
            continue
        cid = m.group(1)
        if cid in seen:
            continue
        seen.add(cid)
        out.append((cid, title))
    return out


LENGTH_RE = re.compile(r'"lengthSeconds":"(\d+)"')


def youtube_for(session, cid):
    url = f"{BASE}/class/{cid}/"
    r = session.get(url, timeout=30)
    if r.status_code != 200:
        return None, f"HTTP {r.status_code}"
    ids = list(dict.fromkeys(EMBED_RE.findall(r.text)))
    # ignore helper/path tokens, keep real video ids
    ids = [i for i in ids if i.lower() not in ("iframe_api", "embed", "watch")]
    if not ids:
        return None, "no video found"
    return f"https://www.youtube.com/watch?v={ids[0]}", "ok"


def video_duration(session, video_url):
    """Return (seconds, 'H:MM:SS') for a YouTube watch URL, or (None, '')."""
    try:
        r = session.get(video_url, timeout=30)
        m = LENGTH_RE.search(r.text)
        if not m:
            return None, ""
        secs = int(m.group(1))
        return secs, format_hms(secs)
    except Exception:
        return None, ""


def format_hms(secs):
    h, rem = divmod(secs, 3600)
    m, s = divmod(rem, 60)
    return f"{h}:{m:02d}:{s:02d}" if h else f"{m}:{s:02d}"


def main():
    session = load_session()
    classes = load_classes()
    print(f"Found {len(classes)} classes. Fetching videos...\n")

    rows = []
    for i, (cid, title) in enumerate(classes, 1):
        try:
            yt, status = youtube_for(session, cid)
        except Exception as e:
            yt, status = None, f"error: {e}"
        secs, dur = video_duration(session, yt) if yt else (None, "")
        rows.append({"title": title, "youtube_url": yt or "",
                     "duration": dur, "duration_seconds": secs or "",
                     "class_id": cid,
                     "class_url": f"{BASE}/class/{cid}/", "status": status})
        flag = "OK " if yt else "-- "
        print(f"[{i:>3}/{len(classes)}] {flag} {dur:>9}  {title[:50]}")
        time.sleep(0.3)

    write_csv(rows)
    write_xlsx(rows)
    write_docx(rows)

    found = sum(1 for r in rows if r["youtube_url"])
    total = sum(int(r["duration_seconds"]) for r in rows if r["duration_seconds"])
    print(f"\nDone. {found}/{len(rows)} classes had a YouTube video.")
    print(f"Total runtime: {format_hms(total)} (h:mm:ss)")
    print("Outputs: classes.csv, classes.xlsx, classes.docx")


def write_csv(rows):
    import csv
    with open(HERE / "classes.csv", "w", newline="", encoding="utf-8-sig") as f:
        w = csv.DictWriter(f, fieldnames=["title", "youtube_url", "duration",
                                          "duration_seconds", "class_id",
                                          "class_url", "status"])
        w.writeheader()
        w.writerows(rows)


def write_xlsx(rows):
    from openpyxl import Workbook
    from openpyxl.styles import Font
    wb = Workbook()
    ws = wb.active
    ws.title = "Classes"
    headers = ["#", "Class Name (Title)", "YouTube URL", "Duration",
               "Seconds", "Class Page", "Status"]
    ws.append(headers)
    for c in ws[1]:
        c.font = Font(bold=True)
    for i, r in enumerate(rows, 1):
        ws.append([i, r["title"], r["youtube_url"], r["duration"],
                   r["duration_seconds"], r["class_url"], r["status"]])
        if r["youtube_url"]:
            cell = ws.cell(row=i + 1, column=3)
            cell.hyperlink = r["youtube_url"]
            cell.font = Font(color="0563C1", underline="single")
    widths = [5, 70, 45, 11, 9, 45, 14]
    for col, wd in enumerate(widths, 1):
        ws.column_dimensions[ws.cell(row=1, column=col).column_letter].width = wd
    ws.freeze_panes = "A2"
    wb.save(HERE / "classes.xlsx")


def write_docx(rows):
    from docx import Document
    doc = Document()
    doc.add_heading("AlgoUniversity Classes", level=1)
    table = doc.add_table(rows=1, cols=3)
    table.style = "Light Grid Accent 1"
    hdr = table.rows[0].cells
    hdr[0].text = "Class Name (Title)"
    hdr[1].text = "YouTube URL"
    hdr[2].text = "Duration"
    for r in rows:
        cells = table.add_row().cells
        cells[0].text = r["title"]
        cells[1].text = r["youtube_url"] or "(no video)"
        cells[2].text = r["duration"]
    doc.save(HERE / "classes.docx")


if __name__ == "__main__":
    main()
