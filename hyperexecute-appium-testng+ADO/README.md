# HyperExecute Mobile Appium Testing Guide

<img height="80" alt="hyperexecute_logo" src="https://user-images.githubusercontent.com/1688653/159473714-384e60ba-d830-435e-a33f-730df3c3ebc6.png">

HyperExecute is a smart test orchestration platform to run end-to-end tests at the fastest speed possible. HyperExecute lets you achieve an accelerated time to market by providing a test infrastructure that offers optimal speed, test orchestration, and detailed execution logs.

The overall experience helps teams test code and fix issues at a much faster pace. HyperExecute is configured using a YAML file. Instead of moving the Hub close to you, HyperExecute brings the test scripts close to the Hub!

* **HyperExecute HomePage**: https://www.lambdatest.com/hyperexecute
* **LambdaTest HomePage**: https://www.lambdatest.com
* **LambdaTest Support**: support@lambdatest.com

To know more about how HyperExecute does intelligent Test Orchestration, check out [HyperExecute Getting Started Guide](https://www.lambdatest.com/support/docs/getting-started-with-hyperexecute/)

---

## Quick Navigation

- [Pre-requisites](#pre-requisites)
- [Step 1: Download HyperExecute CLI](#step-1-download-hyperexecute-cli)
- [Step 2: Choose Your Test Configuration](#step-2-choose-your-test-configuration)
- [Step 3: Run Your Tests](#step-3-run-your-tests)
- [Project Structure](#project-structure)
- [Understanding YAML Configuration](#understanding-yaml-configuration)

---

## Pre-requisites

Before you start, you need:

1. **Download HyperExecute CLI** for your operating system
2. **Grant execute permission** (macOS and Linux only):
   ```bash
   chmod +x ./hyperexecute
   ```

---

## Step 1: Download HyperExecute CLI

The HyperExecute CLI is a tool that runs your tests on the LambdaTest infrastructure. Download the version matching your operating system and save it in your project's root directory (parent folder where the `yaml` folder is located).

| Operating System | Download Link |
|---|---|
| **Windows** | https://downloads.lambdatest.com/hyperexecute/windows/hyperexecute.exe |
| **macOS** | https://downloads.lambdatest.com/hyperexecute/darwin/hyperexecute |
| **Linux** | https://downloads.lambdatest.com/hyperexecute/linux/hyperexecute |

### Grant Execute Permission (macOS and Linux only)

After downloading, open your terminal and navigate to your project's root directory, then run:

```bash
chmod +x ./hyperexecute
```

This command gives the file permission to execute.

**For macOS users:** If you see a security popup, allow it by going to **System Preferences → Security & Privacy → General tab** and clicking "Allow".

---

## Step 2: Choose Your Test Configuration

This project has YAML configuration files organized by platform and device type. Select the one that matches your testing needs:

### Android Testing

**Android Emulator:**
- Single Device: `yaml/android/emulator/hyp-emulator-android-single.yaml`
- Multiple Devices (Parallel): `yaml/android/emulator/hyp-emulator-android-multiple.yaml`

**Android Real Device:**
- Single Device: `yaml/android/realdevice/hyp-rd-android-single.yaml`
- Multiple Devices (Parallel): `yaml/android/realdevice/hyp-rd-android-multiple.yaml`

### iOS Testing

**iOS Simulator:**
- Single Device: `yaml/ios/simulator/hyp-sim-ios-single.yaml`

**iOS Real Device:**
- Single Device: `yaml/ios/realdevice/hyp-rd-ios-single.yaml`
- Multiple Devices (Parallel): `yaml/ios/realdevice/hyp-rd-ios-multiple.yaml`

### Next Steps After Selecting Your YAML File:

1. Open the chosen YAML file in a text editor
2. Find the `app:` field and replace it with your App URL from Step 3 (the `lt://APP...` value)
3. Update any device names or specifications if needed for your testing scenario
4. Save the file

---

## Step 3: Run Your Tests

Open your terminal, navigate to your project's root directory, and run the appropriate command based on how you set up your environment variables.

### Option A: If You Set Environment Variables (Recommended)

```bash
./hyperexecute --config yaml/android/realdevice/hyp-rd-android-single.yaml
```

Replace the path with your chosen YAML file.

### Option B: If You Want to Specify Credentials in Command

```bash
./hyperexecute --user YOUR_USERNAME --key YOUR_ACCESS_KEY --config yaml/android/realdevice/hyp-rd-android-single.yaml
```

Replace `YOUR_USERNAME` and `YOUR_ACCESS_KEY` with your credentials, and update the YAML path as needed.

### What Happens Next

- HyperExecute will start executing your tests on LambdaTest's infrastructure
- You'll see real-time logs in your terminal showing test progress
- After completion, you'll get a summary of passed/failed tests
- Detailed execution logs are available in your LambdaTest dashboard

---

## Project Structure

Here's how your project is organized:

```
project-root/
├── hyperexecute                    (CLI tool you downloaded)
├── yaml/
│   ├── android/
│   │   ├── emulator/
│   │   │   ├── hyp-emulator-android-single.yaml
│   │   │   └── hyp-emulator-android-multiple.yaml
│   │   └── realdevice/
│   │       ├── hyp-rd-android-single.yaml
│   │       └── hyp-rd-android-multiple.yaml
│   └── ios/
│       ├── simulator/
│       │   └── hyp-sim-ios-single.yaml
│       └── realdevice/
│           ├── hyp-rd-ios-single.yaml
│           └── hyp-rd-ios-multiple.yaml
├── src/                            (Your test code)
├── pom.xml                         (Maven configuration)
└── README.md
```

---

## Understanding YAML Configuration

Each YAML file contains test instructions. Here's what the key settings mean:

```yaml
version: 0.2                          # Configuration format version
globalTimeout: 150                    # Total time limit for entire test (in seconds)
testSuiteTimeout: 150                 # Time limit for each test suite
testSuiteStep: 150                    # Time limit for each test step
runson: linux                         # Operating system to run tests on
concurrency: 5                        # Number of parallel tests (for multiple device configs)
autosplit: true                       # Auto-distribute tests across devices
retryOnFailure: false                 # Automatically retry failed tests
maxRetries: 1                         # Maximum retry attempts

pre:                                  # Commands to run before tests
  - mvn -Dmaven.repo.local=./.m2 dependency:resolve

appium: true                          # Enable Appium framework
framework:
  name: maven/testng                  # Test framework (Maven + TestNG)
  discoveryType: xmltest              # How to discover tests

jobLabel: ['HYP-RD', 'Android']       # Tags for identifying your test job
```

---

## Common Issues & Solutions

**"Command not found" error:**
- Make sure the `hyperexecute` file is in your project's root directory
- On macOS/Linux, ensure you ran `chmod +x ./hyperexecute`

**"Permission denied" error on macOS/Linux:**
- Run: `chmod +x ./hyperexecute`

**"Unauthorized" error:**
- Double-check your username and access key are correct
- Verify environment variables are set with `echo $LT_USERNAME`

**App upload fails:**
- Verify the file path to your app is correct
- Check that the file format is supported (.apk, .aab, or .ipa)

**Tests don't start:**
- Ensure the App URL in your YAML is the one from Step 3
- Verify the YAML file path in your command is correct
- Check that your test code and configuration files exist

---

## Documentation & Resources

For more information, visit:

- [LambdaTest Documentation](https://www.lambdatest.com/support/docs/)
- [LambdaTest Blog](https://www.lambdatest.com/blog/)
- [LambdaTest Learning Hub](https://www.lambdatest.com/learning-hub/)
- [LambdaTest Community](https://community.lambdatest.com/)

---

## For Allure Report

Allure Dependencies (pom.xml)

<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>2.25.0</version>
</dependency>

<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-java-commons</artifactId>
    <version>2.25.0</version>
</dependency>

Allure Results Configuration

Allure results are generated under:

reports/allure-results


Configured in Maven Surefire:

<systemPropertyVariables>
    <allure.results.directory>
        reports/allure-results
    </allure.results.directory>
</systemPropertyVariables>


This configuration supports parallel execution safely.

🚀 HyperExecute Allure Report Configuration

The following report configuration has been added to all HyperExecute YAML files:

report: true
partialReports:
  location: reports
  type: html
  frameworkName: allure

What this does:

Enables report generation in HyperExecute

Collects Allure results from the reports directory

Generates HTML Allure reports

Makes reports available in the HyperExecute dashboard



## About LambdaTest

[LambdaTest](https://www.lambdatest.com) is a leading test execution and orchestration platform that is fast, reliable, scalable, and secure. It allows users to run both manual and automated testing of web and mobile apps across 3000+ different browsers, operating systems, and real device combinations. Over 500 enterprises and 1 Million+ users across 130+ countries rely on LambdaTest for their testing needs.

---

## Support

We're available 24/7 to help!

- **Email**: support@lambdatest.com
- **Website**: https://www.lambdatest.com
- **Community**: https://community.lambdatest.com/