<p align="center">
    <a href="https://dscvit.com">
        <img src="https://user-images.githubusercontent.com/30529572/72455010-fb38d400-37e7-11ea-9c1e-8cdeb5f5906e.png" />
    </a>
    <h2 align="center">Keats Android</h2>
    <h4 align="center">A collaborative book reading platform that brings readers together through shared literary experiences</h4>
</p>

<p align="center">
    [![License](https://img.shields.io/github/license/GDGVIT/keats-android?color=blue&style=flat-square)](https://github.com/GDGVIT/keats-android/blob/master/LICENSE)
    [![Download APK](https://img.shields.io/badge/Download-APK-success?style=flat-square&logo=android)](./app/release/app-release.apk)
    [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat-square)](https://android-arsenal.com/api?level=21)
    [![DSC VIT](https://img.shields.io/badge/DSC-VIT-blue?style=flat-square)](https://dsc.community.dev/vellore-institute-of-technology/)
</p>

<p align="center">
    [![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlin.link/)
    [![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/)
    [![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://firebase.google.com/)
    [![Retrofit](https://img.shields.io/badge/Retrofit-009639?style=flat-square&logo=square&logoColor=white)](https://square.github.io/retrofit/)
    [![Architecture Components](https://img.shields.io/badge/Architecture%20Components-4285F4?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture)
</p>

---

## 📖 About

Keats represents a paradigm shift in how we approach literature and learning, transforming reading from an isolated activity into a vibrant, collaborative experience that strengthens educational outcomes and builds meaningful communities. Named after the renowned English Romantic poet John Keats, this innovative platform addresses the growing need for digital literacy tools that foster engagement, critical thinking, and social connection in our increasingly connected world.

By democratizing access to collaborative reading experiences, Keats empowers educators, students, book clubs, and reading enthusiasts to create inclusive learning environments where knowledge is shared, discussions flourish, and literary appreciation transcends geographical boundaries. The platform serves as a catalyst for lifelong learning, promoting literacy while building the social connections that are essential for human development.

## 🌟 Transformative Features & Impact

### 📚 Inclusive Digital Library Experience
- **Universal Format Support**: Breaking down barriers with seamless PDF and ePUB compatibility, ensuring no reader is excluded based on their preferred format or device capabilities
- **Cloud-Powered Accessibility**: Firebase-backed storage democratizes access to literature, allowing users to build and share libraries regardless of their physical storage limitations
- **Cross-Device Continuity**: Synchronized reading progress enables uninterrupted learning across smartphones, tablets, and computers, accommodating diverse learning preferences and accessibility needs

### 🏛️ Community-Driven Learning Ecosystems
- **Flexible Social Architecture**: Private clubs enable intimate study groups and academic discussions, while public communities foster broader literary discourse and cultural exchange
- **Barrier-Free Participation**: QR code and invitation code systems eliminate technical friction, making it effortless for users of all technical skill levels to join reading communities
- **Democratic Moderation**: Thoughtful administrative controls ensure safe, productive spaces while preserving the autonomy and voice of every participant

### 💬 Real-Time Collaborative Intelligence
- **Contextual Discourse**: Page-specific annotation systems transform passive reading into active, collaborative interpretation, enhancing comprehension and critical analysis skills
- **Synchronized Learning Journeys**: Real-time reading progress visibility creates accountability and encouragement within learning communities, fostering motivation and engagement
- **Rich Communication Tools**: Advanced text formatting capabilities enable nuanced expression of ideas, supporting both casual discussions and rigorous academic discourse

### 🔐 Trust & Digital Citizenship
- **Secure Authentication Framework**: OTP-based verification ensures authentic participation while protecting user privacy, building trust in digital learning environments
- **Firebase Security**: Leveraging Google's robust security infrastructure for reliable data protection and user authentication
- **Privacy-Conscious Design**: Thoughtful data handling practices and user-controlled privacy settings empower individuals to participate comfortably while maintaining personal boundaries

### 🌍 Societal Impact & Educational Value
- **Breaking Educational Barriers**: Enables remote and underserved communities to access collaborative learning opportunities traditionally available only in well-resourced institutions
- **Fostering Critical Thinking**: Collaborative annotation and discussion features develop analytical skills essential for civic engagement and informed citizenship
- **Building Empathy**: Shared reading experiences across diverse communities promote cultural understanding and emotional intelligence
- **Supporting Multilingual Learning**: The platform's architecture supports diverse linguistic communities, promoting inclusive education and cultural preservation

## 🏗️ Architecture & Technology Stack

### **Core Technologies**
- **Language**: Kotlin (100%)
- **Platform**: Android (API 21+)
- **Architecture**: MVVM with Repository Pattern
- **UI Framework**: Material Design Components

### **Key Libraries & Frameworks**
- **Dependency Injection**: Dagger Hilt
- **Navigation**: Navigation Component with Safe Args
- **Networking**: Retrofit + OkHttp + Moshi
- **Image Loading**: Glide
- **Authentication**: Firebase Auth
- **Storage**: Firebase Storage
- **Analytics**: Firebase Analytics & Crashlytics
- **QR Code**: ZXing Core + Code Scanner
- **Testing**: JUnit + Espresso

### **Development Tools**
- **Build System**: Gradle
- **Code Quality**: KtLint
- **Version Control**: Git
- **IDE**: Android Studio

## 🚀 Getting Started

### Prerequisites
- Android Studio Bumblebee (2021.1.1) or later
- Android SDK (API 21+)
- JDK 8 or higher
- Firebase project setup

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/GDGVIT/keats-android.git
   cd keats-android
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Configure Firebase**
   - Create a new Firebase project at the [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication, Storage, and Crashlytics
   - Download `google-services.json` and place it in the `app/` directory

4. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

### Configuration
Update the base API URL in `app/src/main/java/com/dscvit/keats/utils/Constants.kt` to point to your backend server.

## 📱 Usage

1. **Account Creation**: Sign up using your phone number with OTP verification
2. **Profile Setup**: Complete your profile with personal details
3. **Join/Create Clubs**: Browse public clubs or create your own reading group
4. **Upload Books**: Add PDF or ePUB files to your personal library
5. **Start Reading**: Begin reading with your club members and engage in discussions

## 🔗 Related Projects

- **[Keats Frontend (Web)](https://github.com/GDGVIT/keats-frontend)** - React-based web interface
- **[Keats Backend](https://github.com/GDGVIT/keats-backend)** - Node.js API server
- **[Keats iOS](https://github.com/GDGVIT/keats-ios)** - Native iOS application

## 🤝 Contributing

We welcome contributions from the community! Please read our [Contributing Guidelines](contributing.md) and [Code of Conduct](CODE_OF_CONDUCT.md) before submitting pull requests.

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Contributors

<table>
    <tr align="center">
        <td>
            <strong>Md Hishaam Akhtar</strong>
            <p align="center">
                <img src="https://user-images.githubusercontent.com/58990970/103586688-9cde9700-4f0b-11eb-915c-0d8b9a555159.JPG" width="150" height="150" alt="Md Hishaam Akhtar">
            </p>
            <p align="center">
                <a href="https://github.com/mdhishaamakhtar">
                    <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="100"/>
                </a>
                <a href="https://www.linkedin.com/in/mdhishaamakhtar">
                    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="100"/>
                </a>
            </p>
        </td>
    </tr>
</table>

## 🎯 Project Status

- ✅ **Authentication System** - Complete with OTP verification
- ✅ **Club Management** - Create, join, and manage book clubs
- ✅ **QR Code Integration** - Seamless club joining experience  
- ✅ **Real-time Comments** - Page-specific annotations and discussions
- ✅ **Member Moderation** - Administrative controls for club hosts
- ✅ **File Upload System** - PDF/ePUB upload functionality
- 📋 **Enhanced Reading Experience** - Advanced reader features (Planned)

## 💬 Support & Community

- **Discord**: [Join our community](https://discord.gg/498KVdSKWR)
- **Issues**: [Report bugs or request features](https://github.com/GDGVIT/keats-android/issues)
- **Discussions**: [Community discussions](https://github.com/GDGVIT/keats-android/discussions)

---

<p align="center">
    <strong>Built with ❤️ by <a href="https://dscvit.com">DSC VIT</a></strong>
</p>

<p align="center">
    <sub>Transforming reading from a solitary activity into a shared journey of discovery</sub>
</p>
