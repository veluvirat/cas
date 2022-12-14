const puppeteer = require('puppeteer');
const cas = require('../../cas.js');

(async () => {
    const browser = await puppeteer.launch(cas.browserOptions());
    let page = await cas.newPage(browser);

    console.log("Log in attempt: #1");
    await submitLoginFailure(page);
    await cas.assertInnerTextStartsWith(page, "#content div.banner p", "Authentication attempt has failed");

    console.log("Log in attempt: #2");
    await submitLoginFailure(page);

    console.log("Log in attempt: #3");
    await submitLoginFailure(page);

    await cas.assertInnerText(page, "#content h2", "Access Denied");
    await cas.assertInnerTextContains(page, "#content p", "You've been throttled.");
    
    await browser.close();
})();

async function submitLoginFailure(page) {
    await cas.goto(page, "https://localhost:8443/cas/login");
    await cas.loginWith(page, "casuser", "BadPassword1");
}


