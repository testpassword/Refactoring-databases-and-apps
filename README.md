# Лабораторная №1 #
## Forked from https://github.com/it-pechenushka/ComMat-Lab1 ##

Выбрать рефакторинги из книги https://www.labirint.ru/books/601754/ и провести их для выбранного проекта.

# Лабораторная №2 #

*Описание*

Разработать диалоговое консольное приложение, реализация которого должна быть построена на принципах объектно-ориентированного анализа и проектирования. Диалоговые консольные приложения – консольные программы, работа с которыми ведётся в диалоговом режиме («запрос-ответ»). В качестве запроса может выступать ввод команды, либо выбор пункта меню, выведенного программой в консоль. После получения запроса программа может потребовать ввода необходимых для выполнения запрошенной операции данных. После получения необходимой информации программа осуществляет соответствующее действие, выводит результат работы в консоль и ожидает следующего запроса.

В процессе выполнения работы необходимо:
1. понять, какие существуют сущности и процессы в проектируемой системе;
2. выделить основные функции системы;
3. продумать логику работы пользователя с программой: предусмотреть набор действий пользователя и
множество состояний программы;
4. реализовать объектно-ориентированную модель в виде программы.

*Общие требования к реализации*

- Организация дружелюбного пользовательского интерфейса
    - проверка вводимых пользователем данных
    - вывод адекватных и понятных пользователю сообщений (в особенности – об ошибках).
- Отсутствие, по возможности, жёстко закодированных значений (например, «зашитых» в код путей к файлам, магических чисел).
- Разработанная программа должна получать все необходимые данные из внешнего источника (например, из консоли, файла, базы данных, от удалённого сервера, и т.д.), то есть при запуске не содержит никакой информации.
- Реализация должна быть выполнена на одном из языков: `C#, Java, Kotlin, C++`. Другие языки следует согласовать с преподавателем.
- Архитектурно приложение должно быть представлено клиентским и серверным компонентом (по усмотрению, `REST API`, взаимодействие через `RPC`). Серверный компонент должен обеспечивать сохранение данных о сеансе использования.