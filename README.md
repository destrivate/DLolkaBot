# DLolkaBot

**DLolkaBot** — это современная, легковесная и удобная Java-библиотека для создания ботов в социальной сети **Lolka**.

⚠️ **Проект находится в активной разработке!** API библиотеки может изменяться. Если вы нашли баг или у вас есть предложение по улучшению, пожалуйста, создайте *Issue*.

---

## ✨ Особенности
* **Легкий старт:** Создание бота и регистрация первой команды занимает всего несколько строк кода.
* **Command Manager:** Удобная и гибкая регистрация текстовых команд.
* **Event Publisher:** Отслеживание любых глобальных и специфичных событий (эвентов)
---

## Примеры использования

### 1. Создание и регистрация команды
Пример того, как зарегистрировать текстовую команду `/test`, которая будет отправлять ответное сообщение в гильдию (сервер):

```java
public class Main {
    public static void main(String[] args) {
        DLolkaBot bot = new DLolkaBot("ВАШ_ТОКЕН");

        bot.getCommandManager().register(new Command("/test") {
            @Override
            public void onCommand(MessageContext messageContext) {
                bot.sendMessage(messageContext.getGuildId(), "test command");
            }
        });

        bot.start();
    }
}
```

### 2. Ловля событий (Event Handling)
Вы можете подписываться как на все события сразу (глобально), так и на конкретные действия — например, на создание нового сообщения:

```java
public class Main {
    public static void main(String[] args) {
        DLolkaBot bot = new DLolkaBot("ВАШ_ТОКЕН");

        // Отслеживание абсолютно всех событий
        bot.getEventPublisher().globalEvent(event -> {
            System.out.println("Получено глобальное событие: " + event.toString());
        });

        // Отслеживание только создания сообщений
        bot.getEventPublisher().messageCreateEvent(event -> {
            System.out.println("Новое сообщение! Тип: " + event.getType());
        });

        bot.start();
    }
}
```

---

## 📦 Подключение к проекту (Gradle)

Пока библиотека находится в статусе `SNAPSHOT`, вы можете подключить её локально или через JitPack. Добавьте в свой `build.gradle`:

```groovy
dependencies {
    // Подключение библиотеки
    implementation files('libs/DLolkaBot-1.0-SNAPSHOT.jar') 
}
```
---