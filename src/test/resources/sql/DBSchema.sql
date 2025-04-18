-- Таблица аккаунты
CREATE TABLE IF NOT EXISTS Accounts (
    firstName      VARCHAR(100),
    lastName       VARCHAR(100),
    email          VARCHAR(255) PRIMARY KEY,
    password       VARCHAR(255),
    timestamp      TIMESTAMP

    );
-- TODO для password добавить шифрование что-то типа String encoded = new BCryptPasswordEncoder().encode("plainPassword");
-- Таблица контактов
CREATE TABLE IF NOT EXISTS Contact (
    firstName       VARCHAR(100),
    lastName        VARCHAR(100),
    birthdate       DATE,
    email           VARCHAR(255),
    phone           VARCHAR(50),
    street1         VARCHAR(255),
    street2         VARCHAR(255),
    city            VARCHAR(100),
    stateProvince   VARCHAR(100),
    postalCode      VARCHAR(20),
    country         VARCHAR(100),
    accountsEmail   VARCHAR(255),
    timestamp       TIMESTAMP,

    CONSTRAINT fk_account_email FOREIGN KEY (accountsEmail) REFERENCES Accounts(email)
    );
