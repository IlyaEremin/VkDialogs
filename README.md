##Сопроводительное письмо.

Приложение заняло у меня около 30 часов работы.
Примерно одинаково:

- Отрисовка аватарки из 4-ех разных картинок, скругление.
- Создание монолитной вьюшки для элемента RecyclerView (первый раз занимался таким).
- Поиск в API подходящего метода получения данных (кто же знал что execute работает только на версии 3.0!).
- Рефакторинг и полировка

#####Получение данных с сервера: считаю, что нашел лучшее решение из доступных.

####Что не доработано и о чем я знаю:
- Истечение сессии никак не обрабатывается.
- Если в первых 200 диалогах пользователя меньше 3-ех чатов, автоподгрузка не будет работать.
- Во время скрола срабатывает GC, Это связано с тем, что Glide не переиспользует bitmap'ы или переиспользует их плохо, куча растет ну и вы поняли.
- При парсе ответа с сервера происходит копирование огромного String только чтобы убрать из него пару символов. Это в принципе незаметно пользователю, но с моей точки зрения дико неэффективно.
- Кэширование - не был уверен что оно нужно, на всякий случай решил не делать.

Но, думаю, цель этой работы не создание идеального чата, а прояснение моего скилла.
Надеюсь вам понравится пасхалка. И мой скилл **>m<**