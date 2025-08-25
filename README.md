# HamasStret - Taxi Fare Calculator App

A modern Android application that calculates dynamic taxi fares based on real-time GPS location tracking and distance measurement.

## 🚕 Overview

HamasStret is a taxi fare calculator designed to provide transparent and accurate pricing for taxi services in Papua New Guinea. The app tracks trips in real-time, calculating fares based on actual distance traveled and trip duration.

## ✨ Features

- **Real-time Fare Calculation**: Automatic fare computation based on GPS coordinates
- **Location Tracking**: Continuous GPS monitoring during trips
- **Trip Timer**: Track trip duration with start, pause, resume, and stop functionality
- **Distance Measurement**: Accurate distance calculation using the Haversine formula
- **Transparent Pricing**: Clear breakdown of base fare and per-kilometer charges
- **Google Maps Integration**: Visual mapping and location services
- **Offline Capability**: Works without internet connection for basic fare calculation

## 💰 Pricing Structure

- **Base Fare**: K 3.40 (flat rate)
- **Per-Kilometer Rate**: K 4.30 per km
- **Minimum Distance**: 50 meters before charging begins
- **Currency**: Papua New Guinea Kina (PGK)

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Dependency Injection**: Hilt
- **Maps**: Google Maps SDK
- **Analytics**: Firebase Analytics
- **Crash Reporting**: Firebase Crashlytics
- **Build System**: Gradle with Kotlin DSL

## 📱 Requirements

- **Minimum SDK**: Android 6.0 (API 23)
- **Target SDK**: Android 14 (API 34)
- **Permissions**: Location access (fine and coarse)
- **Google Services**: Google Maps API key required

## 🚀 Getting Started

### Prerequisites

1. Android Studio Arctic Fox or later
2. Google Maps API key
3. Firebase project setup

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/hamasstret.git
   cd hamasstret
   ```

2. Add your Google Maps API key to `local.properties`:
   ```properties
   MAPS_API_KEY=your_google_maps_api_key_here
   ```

3. Sync project with Gradle files

4. Build and run the application

### Build Variants

The app supports multiple build variants with automatic version management:
- **Development**: For testing and development
- **Production**: For release builds

## 🔧 Configuration

### Version Management

Version information is managed in `version.properties`:
- Major, minor, and patch version numbers
- Automatic version code incrementing
- Support for different build variants

### Dependencies

Key dependencies include:
- Google Maps SDK for location services
- Firebase for analytics and crash reporting
- Hilt for dependency injection
- Jetpack Compose for modern UI

## 📊 Usage

1. **Start Trip**: Grant location permissions and tap "Start"
2. **Track Progress**: Monitor real-time fare, distance, and time
3. **End Trip**: Tap "Stop" to finalize the trip
4. **View History**: Access trip details and fare breakdown

## 🏗️ Project Structure

```
app/src/main/java/io/fantastix/hamasstret/
├── MainActivity.kt              # Main application entry point
├── MainViewModel.kt             # Main view model for UI state
├── model/                       # Data models
│   ├── FareResult.kt           # Fare calculation results
│   ├── LocationData.kt         # Location information
│   └── Trip.kt                 # Trip data structure
├── repository/                  # Data layer
│   ├── FareCalculator.kt       # Core fare calculation logic
│   ├── LocationRepository.kt   # Location data management
│   └── TripRepository.kt       # Trip data operations
├── viewmodel/                   # View models for different screens
├── ui/                         # UI components and screens
├── network/                     # Network and API services
├── utils/                       # Utility functions
└── constants/                   # App constants and configuration
```

## 🔒 Permissions

The app requires the following permissions:
- `ACCESS_FINE_LOCATION`: For precise GPS tracking
- `ACCESS_COARSE_LOCATION`: For approximate location when precise is unavailable
- `INTERNET`: For Google Maps and Firebase services
- `FOREGROUND_SERVICE`: For continuous location tracking

## 🧪 Testing

The app includes location simulation tools for development and testing:
- Manual location input
- Location history tracking
- Simulation speed controls
- Test provider support

## 📈 Analytics

Firebase Analytics integration provides insights into:
- App usage patterns
- Trip statistics
- User behavior
- Performance metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation

## 🔄 Version History

- **v1.0.0**: Initial release with basic fare calculation
- **v1.1.0**: Added location simulation and testing tools
- **v1.2.0**: Enhanced UI with Jetpack Compose
- **v1.3.0**: Firebase integration and analytics

---

**Note**: This app is designed for use in Papua New Guinea and uses local currency (PGK) and pricing structures. Adjust pricing constants in `Fares.kt` for use in other regions.