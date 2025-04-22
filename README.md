
# ATF-Java (Automated Testing Framework)

## 📌 Overview
ATF is a custom automated testing framework built with Java using:
- Cucumber (BDD)
- JUnit 5
- RestAssured (API testing)
- Selenium WebDriver (UI testing)
- Spring (DI and context configuration)
- Allure (reporting)

The framework supports both API and UI testing with centralized configuration in `application.properties` and `atf.properties`.

---

## 🚀 Project Structure

```text
src
├── test
│   ├── java
│   │   └── com.example
│   │       ├── api              # API logic and models
│   │       ├── ui               # Page Objects and UI logic
│   │       ├── steps            # Cucumber step definitions
│   │       ├── runners          # Cucumber test runners
│   │       ├── hook             # Initialization logic (PostConstruct, etc.)
│   │       ├── config           # Spring beans and test context configuration
│   │       ├── sql              # H2/SQL in-memory test logic
│   │       ├── ATFAssert        # Custom assertions
│   │       ├── ATFHTTPAssert    # HTTP response assertions
│   │       └── Application      # Spring Boot entry point
│
├── resources
│   ├── features                 # Cucumber .feature files
│   └── *.properties             # Configuration files
│   └── sql                      # Configuration files for sql
```

---

## ⚙️ How to Run Tests

You can run tests using:

### 🖥️ Batch File
```bash
run-tests-with-report.bat
```

### 💻 Cucumber Runner

Press Shift+Alt+F10 
```

---

## 🔐 Authorization

Authorization tokens are stored in `atf.properties` and are automatically applied in the `Authorization` header.  
In future improvements, dynamic token refresh can be implemented.

---

## 🧪 Assertions

The framework includes custom assertion classes `ATFAssert` and `ATFHTTPAssert` to provide:
- clear error messages
- easy-to-read logs
- centralized assertion logic for API/UI

---



## 🛠️ TODO (Recommended Enhancements)

- [ ] Handle expired tokens automatically
- [ ] Mask secrets/tokens in logs


---

## 📄 License

This framework is designed for educational and practice purposes. Feel free to use and extend it 😉
