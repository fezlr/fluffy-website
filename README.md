# Social Network Website

Social Network Website is a backend application built with Spring Boot that implements a user system with authentication, profiles, friend requests, email verification, and token-based security.

---

## Clone and Run
```
git clone https://github.com/your-repo/fluffy.git
cd fluffy

./gradlew clean test
./gradlew bootRun
```

---

## Features

This project includes:

- User registration with email verification
- Authentication with token-based security
- Password reset via email link
- Account activation via verification code
- “People you may know” recommendation system
- Social feed (posts with images and text)
- User profile management
- Friend request system
- Automatic friendship creation on mutual requests

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Gradle / Maven
- JUnit 5
- Mockito
- Lombok
- Jakarta Validation
- JavaMailSender
- Cloudinary API

---

## Architecture

The project follows a layered architecture:

- controller — REST API layer
- service — business logic layer
- repository — data access layer
- entity — JPA entities
- dto — request/response models
- mapper — object mapping layer

## Authentication Flow

The system uses token-based authentication and email verification.

### Registration flow:

1. User registers
2. System sends verification code via email
3. User confirms the code
4. Account becomes activated

<img width="491" height="581" alt="image" src="https://github.com/user-attachments/assets/6b49a19b-b4a9-4d09-8e0a-065a571425c8" />


---

## Password Reset

1. User requests password reset
2. Receives email with reset link
3. Opens link
4. Sets a new password

<img width="655" height="302" alt="image" src="https://github.com/user-attachments/assets/c5be192d-dcdb-4a47-9486-cfae3f55e2c4" />

---

<img width="483" height="391" alt="image" src="https://github.com/user-attachments/assets/62eadbee-1e26-4af6-bb15-1f9239a9dbde" />



---

## Friend System

- Users can send friend requests
- If both users send requests to each other → friendship is created automatically
- Friend requests can be deleted

<img width="870" height="248" alt="image" src="https://github.com/user-attachments/assets/17b37c87-66ef-44e5-9303-fe8c5d8d6a3b" />

---

## User Profile

A user profile contains:

- first name
- last name
- birth date
- city
- about me section
- profile photo

Profiles can be updated after authentication.

<img width="766" height="414" alt="image" src="https://github.com/user-attachments/assets/048bc096-e83e-4406-89ef-75bd1d4aa73d" />

---

## Cloudinary Integration

Used for:

- Profile images
- Media storage in feed

All uploads are stored in cloud storage.

---

## API

### Auth Controller

- POST /api/v1/auth/register
- POST /api/v1/auth/confirm-email/{id}
- POST /api/v1/auth/confirm-code
- POST /api/v1/auth/send-reset-password
- PATCH /api/v1/auth/reset-password

### User API

- PATCH /api/v1/user/update

### Profile

- POST /api/v1/profile/upload-photo
- POST /api/v1/profile/edit

### Chat API

- POST /api/v1/chats/create
- POST /api/v1/chats/direct
- POST /api/v1/chats/upload-photo
- PATCH /api/v1/chats/update/{id}
- GET /api/v1/chats
- DELETE /api/v1/chats/delete/{id}


### Messages API

- POST /api/v1/messages/create
- PATCH /api/v1/messages/update/{id}
- GET /api/v1/messages/chat/{chatId}
- DELETE /api/v1/messages/delete/{id}


### Friend Requests

- POST /api/v1/friend-request/create/{receiverId}
- DELETE /api/v1/friend-request/delete/{receiverId}


### Friends

- DELETE /api/v1/friend/delete/{id}

### Feed

- POST /api/v1/feed/create
- PATCH /api/v1/feed/update/{id}
- GET /api/v1/feed
- DELETE /api/v1/feed/delete/{id}


---

## Environment Variables

You have `.env-example` file in the root. You need to rename it to `.env` file and update values:

#### `Auth`
TOKEN_EXPIRED_TIME_MINUTES=15

#### `DataBase`
DB_NAME=name
DB_USER=user
DB_PASSWORD=password
DB_PORT=5432

#### `OAuth2`
GOOGLE_CLIENT_ID=1234
GOOGLE_CLIENT_SECRET=1234

#### `Mail Sender`
EMAIL_ADDRESS=example@gmail.com
EMAIL_PASSWORD=password

#### `Cloudinary`
CLOUD_NAME=name
API_KEY=1234
API_SECRET=1234

# Important

Never commit .env to repository

# License

Social Network Website is licensed under the MIT License. [See LICENSE for more information](https://github.com/fezlr/social-network-website/blob/main/LICENSE)
