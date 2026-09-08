r"""
Step 1: Manual-login-assisted capture.

Opens an Edge window at the AlgoUniversity classes page. YOU log in
(email + OTP) in that window. Then press Enter here. The script saves your
session cookies and dumps the rendered HTML so the extractor can be built.

Run:  .venv\Scripts\python.exe capture.py
"""
import json
import time
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.edge.options import Options

CLASSES_URL = "https://www.algouniversity.com/classes/"
HERE = Path(__file__).parent
PROFILE_DIR = HERE / "edge_profile"   # persists login between runs
DUMP_HTML = HERE / "classes_page.html"
COOKIES = HERE / "cookies.json"
LINKS_JSON = HERE / "page_links.json"


def main():
    opts = Options()
    opts.add_argument(f"--user-data-dir={PROFILE_DIR}")
    opts.add_argument("--start-maximized")
    # keep window open / reduce automation banners
    opts.add_experimental_option("excludeSwitches", ["enable-automation"])

    driver = webdriver.Edge(options=opts)
    driver.get(CLASSES_URL)

    print("\n" + "=" * 70)
    print("An Edge window is open.")
    print("1. Log in with your email + OTP in that window.")
    print("2. Navigate so your list of CLASSES is visible.")
    print("3. Come back here and press ENTER.")
    print("=" * 70)
    input("\nPress ENTER once your classes are visible in the browser... ")

    # give any lazy content a moment
    time.sleep(2)

    html = driver.page_source
    DUMP_HTML.write_text(html, encoding="utf-8")
    print(f"[saved] rendered HTML -> {DUMP_HTML}")

    cookies = driver.get_cookies()
    COOKIES.write_text(json.dumps(cookies, indent=2), encoding="utf-8")
    print(f"[saved] {len(cookies)} cookies -> {COOKIES}")

    # quick structural hints: every link + every iframe on the page
    anchors = driver.execute_script(
        "return Array.from(document.querySelectorAll('a')).map(a => "
        "({text: a.innerText.trim(), href: a.href}));"
    )
    iframes = driver.execute_script(
        "return Array.from(document.querySelectorAll('iframe')).map(f => f.src);"
    )
    LINKS_JSON.write_text(
        json.dumps({"anchors": anchors, "iframes": iframes}, indent=2),
        encoding="utf-8",
    )
    print(f"[saved] {len(anchors)} links, {len(iframes)} iframes -> {LINKS_JSON}")

    print("\nDone. Leave this terminal; tell Claude it's captured.")
    print("(The browser stays open so the session is preserved.)")
    input("Press ENTER to close the browser when you're done... ")
    driver.quit()


if __name__ == "__main__":
    main()
