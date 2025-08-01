# WebSocket 실시간 채팅 가이드

## 개요

이 문서는 프로젝트별 실시간 채팅 기능을 위한 WebSocket 연결 방법을 설명합니다.

## 기술 스택

- **WebSocket**: SockJS + STOMP 프로토콜
- **서버 엔드포인트**: `/ws`
- **메시지 브로커**: `/topic/project/{projectId}`

## 연결 방법

### 1. WebSocket 연결 설정

```javascript
// SockJS와 STOMP 라이브러리 로드
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

// WebSocket 연결
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

// 연결 시도
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // 연결 성공 후 처리
}, function (error) {
    console.error('STOMP error:', error);
});
```

### 2. 채팅방 구독

```javascript
// 특정 프로젝트의 채팅방 구독
const projectId = 1;
stompClient.subscribe('/topic/project/' + projectId, function (message) {
    const chatMessage = JSON.parse(message.body);
    displayMessage(chatMessage);
});
```

### 3. 채팅방 입장

```javascript
// 채팅방 입장 메시지 전송
const joinMessage = {
    projectId: 1,
    senderEmail: "user@example.com",
    senderNickname: "사용자명",
    content: "사용자명님이 입장했습니다.",
    messageType: "JOIN"
};

stompClient.send("/app/chat.addUser", {}, JSON.stringify(joinMessage));
```

### 4. 메시지 전송

```javascript
// 일반 채팅 메시지 전송
const chatMessage = {
    projectId: 1,
    senderEmail: "user@example.com",
    senderNickname: "사용자명",
    content: "안녕하세요!",
    messageType: "CHAT"
};

stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
```

### 5. 연결 해제

```javascript
// WebSocket 연결 해제
stompClient.disconnect();
```

## 메시지 타입

### 1. JOIN (입장)
```json
{
    "projectId": 1,
    "senderEmail": "user@example.com",
    "senderNickname": "사용자명",
    "content": "사용자명님이 입장했습니다.",
    "messageType": "JOIN"
}
```

### 2. CHAT (일반 메시지)
```json
{
    "projectId": 1,
    "senderEmail": "user@example.com",
    "senderNickname": "사용자명",
    "content": "안녕하세요!",
    "messageType": "CHAT"
}
```

### 3. LEAVE (퇴장)
```json
{
    "projectId": 1,
    "senderEmail": "user@example.com",
    "senderNickname": "사용자명",
    "content": "사용자명님이 채팅방을 나갔습니다.",
    "messageType": "LEAVE"
}
```

## 서버 응답 메시지

서버에서 전송되는 메시지는 다음과 같은 추가 필드를 포함합니다:

```json
{
    "id": "uuid-string",
    "projectId": 1,
    "senderEmail": "user@example.com",
    "senderNickname": "사용자명",
    "content": "메시지 내용",
    "messageType": "CHAT",
    "timestamp": "2025-07-30T22:54:36.125516"
}
```

## 완전한 예시 코드

```javascript
class ChatClient {
    constructor(projectId, userEmail, userNickname) {
        this.projectId = projectId;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.stompClient = null;
    }

    connect() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            
            // 채팅방 구독
            this.subscribeToChat();
            
            // 입장 메시지 전송
            this.joinChat();
        }, (error) => {
            console.error('STOMP error:', error);
        });
    }

    subscribeToChat() {
        this.stompClient.subscribe('/topic/project/' + this.projectId, (message) => {
            const chatMessage = JSON.parse(message.body);
            this.displayMessage(chatMessage);
        });
    }

    joinChat() {
        const joinMessage = {
            projectId: this.projectId,
            senderEmail: this.userEmail,
            senderNickname: this.userNickname,
            content: this.userNickname + '님이 입장했습니다.',
            messageType: 'JOIN'
        };

        this.stompClient.send("/app/chat.addUser", {}, JSON.stringify(joinMessage));
    }

    sendMessage(content) {
        if (this.stompClient && content.trim()) {
            const chatMessage = {
                projectId: this.projectId,
                senderEmail: this.userEmail,
                senderNickname: this.userNickname,
                content: content,
                messageType: 'CHAT'
            };

            this.stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        }
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
    }

    displayMessage(chatMessage) {
        // 메시지 표시 로직 구현
        console.log(`${chatMessage.senderNickname}: ${chatMessage.content}`);
    }
}

// 사용 예시
const chatClient = new ChatClient(1, "user@example.com", "사용자명");
chatClient.connect();

// 메시지 전송
chatClient.sendMessage("안녕하세요!");

// 연결 해제
chatClient.disconnect();
```

## 채팅 히스토리 API

채팅 히스토리를 가져오려면 REST API를 사용하세요:

### 전체 채팅 히스토리
```javascript
fetch('/api/projects/1/chat/history', {
    headers: {
        'Authorization': 'Bearer ' + jwtToken
    }
})
.then(response => response.json())
.then(messages => {
    // 채팅 히스토리 처리
});
```

### 최근 메시지 (기본 50개)
```javascript
fetch('/api/projects/1/chat/recent', {
    headers: {
        'Authorization': 'Bearer ' + jwtToken
    }
})
.then(response => response.json())
.then(messages => {
    // 최근 메시지 처리
});
```

## 주의사항

1. **한 사용자당 하나의 WebSocket 연결만 유지**하세요.
2. **프로젝트를 나갈 때 반드시 연결을 해제**하세요.
3. **메시지 타입은 서버에서 자동으로 설정**되므로 클라이언트에서 설정할 필요가 없습니다.
4. **퇴장 메시지는 자동으로 전송**되므로 별도 처리가 필요하지 않습니다.

## 에러 처리

```javascript
stompClient.connect({}, 
    (frame) => {
        // 성공 처리
    }, 
    (error) => {
        console.error('연결 실패:', error);
        // 재연결 로직 구현
        setTimeout(() => {
            this.connect();
        }, 5000);
    }
);
```

## 테스트

테스트를 위해 제공되는 HTML 페이지를 사용하세요:
- URL: `http://localhost:8080/chat-test.html`
- 여러 브라우저 탭에서 동시 접속하여 실시간 채팅 테스트 가능 