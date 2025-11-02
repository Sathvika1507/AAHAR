# DigitalWell - Your Guide to a Balanced Digital Life

DigitalWell is an Android application built with Java and Firebase that helps users understand and manage their digital habits. The app promotes a balanced relationship with technology through screen time tracking, goal setting, and daily motivational quotes.

## 🌟 Features

### 📱 Screen Time Tracking
- **Real-time Monitoring**: Track your daily, weekly, and monthly screen time usage
- **App Breakdown**: See detailed usage statistics for each app
- **Usage Stats API**: Utilizes Android's UsageStatsManager for accurate tracking
- **Historical Data**: View yesterday's usage and weekly trends

### 🎯 Daily Goals
- **Custom Goals**: Set personalized daily screen time goals
- **Progress Tracking**: Visual progress indicators showing goal achievement
- **Goal Notifications**: Get notified when you're approaching or exceeding your goals
- **Firebase Sync**: Goals are saved to Firebase for persistence across sessions

### 📊 Statistics & Insights
- **Weekly Analytics**: View average daily usage over the past week
- **Peak Usage Days**: Identify your highest and lowest usage days
- **Monthly Trends**: Track long-term usage patterns
- **Visual Charts**: Easy-to-understand data visualization (coming soon)

### 💭 Motivational Quotes
- **Daily Inspiration**: Receive a new motivational quote each day
- **Curated Collection**: 30+ carefully selected quotes about digital wellness
- **Beautiful Design**: Quotes displayed on gradient cards for visual appeal

### 🎨 Professional Design
- **Modern Gradient UI**: Beautiful purple-to-pink gradient design throughout
- **Material Design 3**: Follows latest Android design guidelines
- **Dark Theme**: Eye-friendly dark color scheme
- **Smooth Animations**: Polished transitions and interactions
- **Responsive Layout**: Optimized for all screen sizes

## 📸 Screenshots

### Dashboard
The main dashboard displays:
- Total screen time for today with circular progress indicator
- Daily goal progress
- Motivational quote card with gradient background
- Quick access cards to Goals and Statistics

### Screen Time Details
- Tabbed interface for Today, Yesterday, and This Week
- Total screen time summary
- Detailed app-by-app breakdown
- Swipe-to-refresh functionality

### Goals
- Current goal display with progress bar
- Number pickers for setting hours and minutes
- Real-time goal achievement tracking
- Firebase-backed persistence

### Statistics
- Weekly average screen time
- Peak and lowest usage days
- Monthly statistics
- Chart placeholder for future enhancements

## 🛠️ Technical Stack

### Core Technologies
- **Language**: Java
- **Platform**: Android (Min SDK 24, Target SDK 34)
- **Build System**: Gradle 8.9.2

### Firebase Services
- **Firebase Authentication**: User authentication and email verification
- **Firebase Realtime Database**: Store user goals and preferences
- **Firebase Storage**: Profile picture storage
- **Firebase Analytics**: Track app usage patterns

### Android Components
- **UsageStatsManager**: Screen time tracking
- **RecyclerView**: Efficient list rendering
- **SwipeRefreshLayout**: Pull-to-refresh functionality
- **CardView**: Material Design cards
- **ViewBinding**: Type-safe view access

### UI/UX
- **Material Design Components**: Modern Android UI
- **Custom Gradients**: Professional color schemes
- **Number Pickers**: Intuitive goal setting
- **Progress Indicators**: Visual feedback

## 📋 Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Firebase project with Authentication and Realtime Database enabled
- Google Services JSON configuration file

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DigitalWell
   ```

2. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Firebase Authentication (Email/Password)
   - Enable Firebase Realtime Database
   - Download `google-services.json`
   - Place it in `app/` directory

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

4. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

5. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10

## 🔐 Permissions

The app requires the following permissions:

- **INTERNET**: For Firebase connectivity
- **ACCESS_WIFI_STATE**: Network state monitoring
- **ACCESS_NETWORK_STATE**: Network connectivity checks
- **PACKAGE_USAGE_STATS**: Screen time tracking (requires manual grant)

### Granting Usage Stats Permission

1. Open the app
2. When prompted, tap "Go to Settings"
3. Find DigitalWell in the list
4. Enable "Permit usage access"
5. Return to the app

## 📱 Usage

### First Time Setup

1. **Sign Up**
   - Launch the app
   - Tap "Sign Up"
   - Enter your email, password, name, and phone number
   - Verify your email address

2. **Grant Permissions**
   - Allow usage stats access when prompted
   - This is required for screen time tracking

3. **Set Your Goal**
   - Navigate to Goals from the dashboard
   - Use the number pickers to set your daily screen time goal
   - Tap "Save Goal"

### Daily Use

1. **Check Dashboard**
   - View your daily screen time at a glance
   - Read the motivational quote
   - Monitor goal progress

2. **View Details**
   - Tap "View Details" to see app-by-app breakdown
   - Switch between Today, Yesterday, and This Week tabs
   - Pull down to refresh data

3. **Adjust Goals**
   - Visit the Goals screen to modify your daily target
   - Track your progress in real-time

4. **Review Statistics**
   - Check weekly and monthly trends
   - Identify usage patterns
   - Make informed decisions about your digital habits

## 🎨 Color Scheme

The app uses a professional gradient design:

- **Primary Gradient**: Indigo (#6366F1) → Purple (#8B5CF6) → Pink (#EC4899)
- **Background**: Dark slate (#0F172A to #1E293B)
- **Cards**: Slate gradient (#334155 to #1E293B)
- **Text**: White (#F8FAFC) with varying opacity
- **Accents**: Success green, warning yellow, error red

## 🏗️ Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/aahar100/
│   │   │   ├── MainActivity.java              # Dashboard
│   │   │   ├── ScreenTimeActivity.java        # Screen time details
│   │   │   ├── ScreenTimeAdapter.java         # RecyclerView adapter
│   │   │   ├── GoalsActivity.java             # Goal setting
│   │   │   ├── StatisticsActivity.java        # Usage statistics
│   │   │   ├── ScreenTimeManager.java         # Usage tracking utility
│   │   │   ├── QuoteManager.java              # Quote management
│   │   │   ├── ScreenTimeData.java            # Data model
│   │   │   ├── DailyGoal.java                 # Goal model
│   │   │   ├── Quote.java                     # Quote model
│   │   │   ├── SplashScreen.java              # Splash screen
│   │   │   ├── landing_page.java              # Login
│   │   │   ├── sign_up.java                   # Registration
│   │   │   ├── UserProfileActivity.java       # User profile
│   │   │   ├── setting_activity.java          # Settings
│   │   │   └── ... (other activities)
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_screen_time.xml
│   │   │   │   ├── activity_goals.xml
│   │   │   │   ├── activity_statistics.xml
│   │   │   │   └── item_app_usage.xml
│   │   │   ├── drawable/
│   │   │   │   ├── gradient_background.xml
│   │   │   │   ├── card_gradient.xml
│   │   │   │   ├── button_gradient.xml
│   │   │   │   └── quote_card_gradient.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   └── ...
├── build.gradle
└── google-services.json
```

## 🔧 Configuration

### Firebase Database Structure

```json
{
  "Users": {
    "userId": {
      "name": "User Name",
      "email": "user@example.com",
      "phone": "1234567890",
      "dailyGoal": 7200000,  // in milliseconds
      "profilePicUrl": "https://..."
    }
  }
}
```

### Gradle Dependencies

Key dependencies include:
- Firebase BOM 33.12.0
- Firebase Auth 23.2.0
- Firebase Database 21.0.0
- Material Components 1.12.0
- RecyclerView 1.3.2
- SwipeRefreshLayout 1.1.0
- MPAndroidChart 3.1.0 (for future charts)

## 🐛 Troubleshooting

### Screen Time Not Showing

**Problem**: Screen time displays "--" or "0h 0m"

**Solution**:
1. Ensure usage stats permission is granted
2. Go to Settings → Apps → Special app access → Usage access
3. Enable DigitalWell
4. Restart the app

### Firebase Connection Issues

**Problem**: Data not syncing or authentication failing

**Solution**:
1. Verify `google-services.json` is in the correct location
2. Check Firebase project configuration
3. Ensure internet connectivity
4. Verify Firebase Authentication is enabled

### Build Errors

**Problem**: Gradle sync or build failures

**Solution**:
1. Clean project: Build → Clean Project
2. Rebuild: Build → Rebuild Project
3. Invalidate caches: File → Invalidate Caches / Restart
4. Update Gradle and dependencies

## 🚀 Future Enhancements

- [ ] Interactive charts for usage trends
- [ ] App usage categories (Social, Productivity, Entertainment)
- [ ] Weekly/monthly reports
- [ ] Export data functionality
- [ ] Dark/Light theme toggle
- [ ] Widget for home screen
- [ ] Notification reminders
- [ ] Focus mode to block distracting apps
- [ ] Achievements and badges
- [ ] Social features (compare with friends)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 Contact

For questions, feedback, or support, please contact:
- Email: support@digitalwell.app
- GitHub Issues: [Create an issue](https://github.com/yourusername/digitalwell/issues)

## 🙏 Acknowledgments

- Motivational quotes curated from various sources
- Material Design guidelines by Google
- Firebase platform by Google
- Android UsageStatsManager API
- Open source community

---

**DigitalWell** - Balance your digital life, enhance your real life 🌟

Version 1.0.0 | Built with ❤️ using Java and Firebase
