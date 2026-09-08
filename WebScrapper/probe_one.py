"""Probe a single class page (headless, reusing the logged-in profile)
to discover where the YouTube link lives."""
import re
import sys
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.edge.options import Options

HERE = Path(__file__).parent
PROFILE_DIR = HERE / "edge_profile"

url = sys.argv[1] if len(sys.argv) > 1 else "https://www.algouniversity.com/class/4293"

opts = Options()
opts.add_argument(f"--user-data-dir={PROFILE_DIR}")
opts.add_argument("--headless=new")
opts.add_argument("--window-size=1920,1080")
opts.add_argument("--no-sandbox")
opts.add_argument("--disable-dev-shm-usage")
opts.add_argument("--disable-gpu")
opts.add_argument("--remote-debugging-port=0")

driver = webdriver.Edge(options=opts)
try:
    driver.get(url)
    import time
    time.sleep(4)
    html = driver.page_source
    (HERE / "one_class.html").write_text(html, encoding="utf-8")
    print("TITLE:", driver.title)
    iframes = driver.execute_script(
        "return Array.from(document.querySelectorAll('iframe')).map(f => f.src);")
    print("IFRAMES:", iframes)
    yt = sorted(set(re.findall(r'https?://[^"\'\\s<>]*(?:youtube\.com|youtu\.be)[^"\'\\s<>]*', html)))
    print("YOUTUBE MATCHES:")
    for u in yt:
        print("  ", u)
    print("HTML saved -> one_class.html (", len(html), "bytes )")
finally:
    driver.quit()
