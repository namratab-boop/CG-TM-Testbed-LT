Here is the updated **README.md** file, structured step-by-step to reflect your current project progress and the scripts in your `package.json`. It aligns with your **Phase 2 Rollout Strategy** to ensure "Successful sample executions" and "Unified reporting".

---

# 🚀 QE Studio: Playwright & JMeter Unified Testbed

This repository contains a unified automation framework for **Web/Mobile UI Testing** and **API/Performance Testing**. It supports both JavaScript and TypeScript in a monorepo structure.

## 📂 Project Roadmap (Phase 2)

As per the rollout strategy, we have implemented the following:

1. **Web Automation**: Playwright-based testing in JS and TS.
2. **Performance Testing**: JMeter integration for load and stress testing.
3. **CI/CD Integration**: Automated Azure Pipeline setup.

---

## 🛠️ Local Setup & Installation

### 1. Prerequisites

* **Node.js**: `v18.0.0` or higher (defined in `package.json`).
* **Java (JDK)**: Required to run JMeter locally.
* **JMeter**: Installed on your system (added to System PATH).

### 2. Dependencies

From the root directory, install all functional testing libraries:

```bash
npm install
npx playwright install --with-deps

```

---

## 🧪 Running Functional Tests (Playwright)

Use the following scripts defined in the `package.json` to execute UI tests:

| Script | Command | Purpose |
| --- | --- | --- |
| **TypeScript** | `npm run test:ts` | Runs Playwright TS tests using the config in `/Playwright-TS/` |
| **JavaScript (Single)** | `npm run test:js` | Runs a single JS test file using Node |
| **JavaScript (Parallel)** | `npm run test:js-parallel` | Runs JS tests in parallel mode |
| **Run All UI** | `npm run test:all` | Sequentially runs both TS and JS suites |

---

## 📈 Running Performance Tests (JMeter)

Performance tests are executed via the CLI to support Phase 2 "CI/CD-based automated test pipelines".

**Command to execute locally:**

```powershell
# Run from the project root
jmeter -n -t Performance-JMeter/performance.jmx -l results.jtl -e -o ./Performance-JMeter/reports

```

### Analyzing Results

1. **GUI Mode**: Open `jmeter.bat`, add a **View Results Tree** listener, and browse for the `results.jtl` file.
2. **HTML Dashboard**: Open `Performance-JMeter/reports/index.html` in your browser for graphical performance metrics.

---

## ☁️ CI/CD Integration (Azure DevOps)

The `Azure_pipeline.yaml` is configured to trigger on every push to the `main` branch.

* **Functional**: Executes Playwright suites.
* **Performance**: Installs JMeter and runs `performance.jmx`.
* **Artifacts**: Publishes a unified **Performance Dashboard** as a build artifact.

---

## 🧹 Git Best Practices

To keep the repo clean, ensure your `.gitignore` excludes local artifacts:

* `results.jtl` (Raw performance data).
* `jmeter.log` (Local execution logs).
* `reports/` and `test-results/` (Generated output).

---

### What should you do next?

Since your README and local setup are now complete, **would you like me to show you how to commit these changes to your GitHub "origin" and verify if the Azure Pipeline triggers correctly?**