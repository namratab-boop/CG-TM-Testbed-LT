require('dotenv').config();
const { chromium } = require('playwright')
const { expect } = require("expect");
const cp = require('child_process');
const playwrightClientVersion = cp.execSync('npx playwright --version').toString().trim().split(' ')[1];

const parallelTests = async (capability) => {
    const testName = capability['LT:Options']['name'];
    const platform = capability['LT:Options']['platform'];
    const browser = capability['browserName'];

    console.log(`[${testName}] Starting test on ${platform}...`);

    const browserInstance = await chromium.connect({
        wsEndpoint: `wss://cdp.lambdatest.com/playwright?capabilities=${encodeURIComponent(JSON.stringify(capability))}`
    })

    const page = await browserInstance.newPage()

    try {
        // --- STEP 1: LOGIN ---
        console.log(`[${testName}] Navigating to Ecommerce site...`);
        await page.goto('https://ecommerce-playground.lambdatest.io/', { timeout: 30000 });

        await page.locator('a[role="button"] >> span:has-text("My account")').hover();
        await page.locator('span', { hasText: 'Login' }).click();
        expect(await page.title()).toBe('Account Login');

        await page.locator('#input-email').fill('saranshk@lambdatest.com');
        await page.locator('#input-password').fill('Skum@1009');
        await page.locator('input[value="Login"]').click();
        expect(await page.title()).toBe('My Account');

        // --- STEP 2: SEARCH ---
        console.log(`[${testName}] Searching for iPhone...`);
        await page.locator('xpath=(//input[@placeholder="Search For Products"])[1]').fill('iPhone');
        await page.locator('button', { hasText: 'Search' }).click();

        // --- STEP 3: FILTER ---
        console.log(`[${testName}] Applying filters...`);
        await page.waitForTimeout(3000);
        await page.locator('xpath=(//div[contains(@class, "custom-checkbox")]//label[@class="custom-control-label"][text()="In stock"])[2]').click();

        // --- STEP 4: ADD TO CART ---
        console.log(`[${testName}] Adding to cart...`);
        await page.waitForTimeout(3000);
        await page.locator('h4.title >> a[href*="product_id=78"]').click();
        await page.waitForTimeout(3000);
        await page.locator('xpath=//div[contains(@class,"no-gutters")]//button[@title="Add to Cart"][contains(@class,"cart-78")]').click();

        // --- STEP 5: VIEW & REMOVE ---
        console.log(`[${testName}] Viewing and clearing cart...`);
        await page.locator('#notification-box-top >> a:has-text("View Cart")').click();
        await page.waitForTimeout(3000);
        const isVisible = await page.locator('td.text-left >> a:has-text("iPhone")').isVisible();
        expect(isVisible).toBe(true);
        await page.locator('i[class*="fa-times-circle"]').click();

        // --- STEP 6: LOGOUT ---
        console.log(`[${testName}] Logging out...`);
        await page.waitForTimeout(3000);
        await page.locator('a[role="button"] >> span:has-text("My account")').hover();
        await page.locator('span', { hasText: 'Logout' }).click();
        await page.waitForTimeout(2000);
        expect(await page.title()).toBe('Account Logout');

        // Success Reporting
        await page.evaluate(_ => {}, `lambdatest_action: ${JSON.stringify({
            action: 'setTestStatus',
            arguments: { status: 'passed', remark: 'Ecommerce flow passed' }
        })}`)

        console.log(`[${testName}] PASSED!`);
        await teardown(page, browserInstance)

    } catch (e) {
        console.log(`[${testName}] FAILED! Error: ${e.message}`);

        // Failure Reporting
        await page.evaluate(_ => {}, `lambdatest_action: ${JSON.stringify({
            action: 'setTestStatus',
            arguments: { status: 'failed', remark: e.message }
        })}`)

        await teardown(page, browserInstance)
        throw e.stack
    }
}

async function teardown(page, browser) {
    await page.close();
    await browser.close();
}

// Capabilities array
const capabilities = [
    {
        'browserName': 'Chrome',
        'browserVersion': 'latest',
        'LT:Options': {
            'platform': 'Windows 10',
            'build': 'Playwright JS Build',
            'name': 'Sample Test',
            'user': process.env.LT_USERNAME,
            'accessKey': process.env.LT_ACCESS_KEY,
            'network': true,
            'video': true,
            'console': true,
            'playwrightClientVersion': playwrightClientVersion,
            'goog:chromeOptions': ['--start-maximized']
        }
    },
    {
        'browserName': 'MicrosoftEdge',
        'browserVersion': 'latest',
        'LT:Options': {
            'platform': 'MacOS Ventura',
            'build': 'Playwright JS Build',
            'name': 'Sample Test',
            'user': process.env.LT_USERNAME,
            'accessKey': process.env.LT_ACCESS_KEY,
            'network': true,
            'video': true,
            'console': true,
            'playwrightClientVersion': playwrightClientVersion,
            'goog:chromeOptions': ['--start-maximized']
        }
    },
    {
        'browserName': 'Chrome',
        'browserVersion': 'latest',
        'LT:Options': {
            'platform': 'MacOS Big sur',
            'build': 'Playwright JS Build',
            'name': 'Sample Test',
            'user': process.env.LT_USERNAME,
            'accessKey': process.env.LT_ACCESS_KEY,
            'network': true,
            'video': true,
            'console': true,
            'playwrightClientVersion': playwrightClientVersion,
            'goog:chromeOptions': ['--start-maximized']
        }
    }
]

const executeTests = async () => {
    console.log('Launching Ecommerce parallel tests...');
    try {
        const promises = capabilities.map(capability => parallelTests(capability));
        await Promise.all(promises);
        console.log('All parallel tests completed!');
    } catch (error) {
        console.error('Error in parallel execution:', error);
        process.exit(1);
    }
};

executeTests();