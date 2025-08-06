AI Context & Ethical Guidelines
Repository: hamas-stret

1. Project Overview
* This project involves web scraping and data analysis of publicly available content. It may include:
* Automated collection of text/data from news or social media.
* NLP (Natural Language Processing) for keyword extraction, sentiment analysis, or topic modeling.
* No affiliation with any political or militant entities.

2. Language Note: "Hamas" in Tok Pisin
* The word "Hamas" in Tok Pisin (Papua New Guinea) means:
* "Happy" (Hamamas) or "How much?" (Hamas?) depending on context.
* This is unrelated to the Middle Eastern group of the same name.

If the project processes Tok Pisin data, ensure linguistic clarity to avoid misinterpretation.

3. AI/ML Components
   Tools Used: (Example: Python, BeautifulSoup, NLTK, Transformers)

Purpose:

Scraping and cleaning data.

Analyzing trends (e.g., frequency of terms like "Hamas" in different linguistic contexts).

4. Ethical Considerations
   Bias Mitigation:

Explicitly filter out false positives (e.g., Tok Pisin "Hamas" vs. other meanings).

Data Sources:

Only use publicly available data with proper attribution.

Avoid private or sensitive content.

Transparency:

Document limitations (e.g., potential ambiguities in language analysis).

5. Setup & Usage
   Install dependencies: pip install -r requirements.txt

Configure API keys (if applicable) in .env.



AI Context: Cab Fare Calculator App
Repository: hamas-stret

1. Core Functionality
   This Android app calculates dynamic cab fares based on:

Real-time coordinates (latitude/longitude) for distance tracking.

Time-based pricing (e.g., surge hours, night rates).

Potential integration with maps APIs (e.g., Google Maps, OSRM).

2. AI/ML Components (If Applicable)
   (Remove or expand if no AI is used)

Predictive Pricing:

Machine learning models (e.g., linear regression) to adjust fares based on traffic, demand, or historical data.

Route Optimization:

Algorithms (e.g., Dijkstra’s, A*) for efficient pathfinding.

Tools: TensorFlow Lite (for on-device ML), Firebase ML Kit.

3. Data Usage & Privacy
   Collected Data:

GPS coordinates (for route/distance).

Timestamps (for time-based pricing).

No persistent storage of user location (if compliant with privacy laws).

Permissions:

ACCESS_FINE_LOCATION (required for precise fare calculation).

4. Ethical & Legal Considerations
   Transparency:

Clearly disclose fare calculation logic to users (e.g., "Base fare + ₹X/km").

Bias Mitigation:

Ensure surge pricing doesn’t disproportionately affect areas (audit algorithms if ML-driven).

Compliance:

GDPR/regional laws: Allow users to delete trip history.

5. Setup for Developers
   Dependencies:

Android Studio, Google Maps SDK, Retrofit (for API calls).

Add ML model files (if used) to app/src/main/ml/.

Environment Variables:

Store API keys (e.g., Google Maps) in local.properties.