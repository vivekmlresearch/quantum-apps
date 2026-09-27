#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p /tmp/quantum-apps-tests
javac -d /tmp/quantum-apps-tests app/src/main/java/com/vivekmlresearch/quantumapps/core/*.java app/src/test/java/com/vivekmlresearch/quantumapps/core/*.java
java -cp /tmp/quantum-apps-tests com.vivekmlresearch.quantumapps.core.EngineCheck
