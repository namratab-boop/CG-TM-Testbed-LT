import { defineConfig } from '@playwright/test';
import dotenv from 'dotenv';

dotenv.config();

export default defineConfig({
    testDir: './tests',
    timeout: 60000,

    use: {
        connectOptions: {
            wsEndpoint: `wss://cdp.lambdatest.com/playwright?capabilities=${encodeURIComponent(JSON.stringify({
                browserName: 'Chrome',
                browserVersion: 'latest',
                'LT:Options': {
                    platform: 'Windows 11',
                    build: 'Playwright TS Build',
                    name: 'Sample Test',
                    user: process.env.LT_USERNAME,
                    accessKey: process.env.LT_ACCESS_KEY,
                    'goog:chromeOptions': ['--start-maximized']
                }
            }))}`
        }
    }
});