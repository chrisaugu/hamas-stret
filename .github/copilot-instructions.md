# Copilot Instructions for hamas-stret

## Project Overview
- This repository contains two main components:
  1. **Web Scraping & NLP Analysis**: Automated collection and analysis of public text data, with a focus on linguistic ambiguity (e.g., Tok Pisin "Hamas" vs. other meanings).
  2. **Cab Fare Calculator Android App**: Calculates dynamic fares using real-time GPS, time-based pricing, and optional ML for predictive pricing and route optimization.

## Architecture & Key Directories
- `app/`: Android app source code. Key subfolders:
  - `src/main/`: Main app code, activities, services, ML models (if used).
  - `src/test/`, `src/androidTest/`: Unit and instrumentation tests.
  - `build.gradle.kts`: App-level build config.
- Root-level scripts:
  - `build.gradle.kts`, `settings.gradle.kts`: Project build and settings.
  - `local.properties`: Store API keys (e.g., Google Maps).
- ML/data analysis code (if present) is typically in Python, not included in this repo structure.

## Developer Workflows
- **Build/Run Android App**: Use Android Studio or `./gradlew assembleDebug`.
- **Testing**: Run unit tests with `./gradlew test` and instrumentation tests with `./gradlew connectedAndroidTest`.
- **Dependency Management**: Managed via Gradle (`libs.versions.toml`).
- **API Keys**: Store in `local.properties`, never commit secrets.
- **ML Model Integration**: Place TensorFlow Lite/Firebase ML Kit models in `app/src/main/ml/`.

## Project-Specific Conventions
- **Ethical Data Use**: Only scrape public data, filter linguistic ambiguities, document limitations.
- **Fare Calculation Logic**: Clearly disclose formulas (see AI_CONTEXT.md for details).
- **Permissions**: Request only necessary Android permissions (e.g., ACCESS_FINE_LOCATION).
- **No persistent location storage** unless required by law/compliance.

## Integration Points
- **Google Maps SDK**: For route and location services.
- **ML Libraries**: TensorFlow Lite, Firebase ML Kit (if used).
- **External APIs**: Configure keys in `local.properties`.

## Patterns & Examples
- **Surge Pricing**: Implement time-based logic in fare calculation modules.
- **Route Optimization**: Use standard algorithms (Dijkstra, A*) for pathfinding if ML is enabled.
- **Bias Mitigation**: Audit ML models for fairness (see AI_CONTEXT.md).

## References
- See `AI_CONTEXT.md` for ethical guidelines and project context.
- See `README.md` for high-level project info.
- See `app/build.gradle.kts` for dependencies and build setup.

---
_If any section is unclear or missing, please provide feedback for further refinement._
