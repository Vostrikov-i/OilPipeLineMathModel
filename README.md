# Mathematical modeling of pipeline transportation of oil
### [ENG]
This model is calculating pressures and flow rate of pipeline.

Common description:

Pipeline starts at a tank. The tank has constant oil level and constant output flow speed
Pipeline transportation is divided into sections. Pressure and flow velocity are calculated in each section. It depend on pressure and flow velocity in the previous and next sections.
Pipeline ends at a tank too. You get 0 pressure value if there's no end tank.

Avaliable objects:
 - Pipe
 - Dumper (or Valve)
 - Pump
 - Check valve (Sorry for my English. At code it's called not return valve)
 - Tank
 
Limits:
1. This model is only for laminar flow (without air bubles)
2. Density is 960 kg/m3
3. Speed of sound in the oil 1221 m/s
4. Input flow rate 10 000 m3/h
5. Viscosity 300 mm2/s (I know that it's not a good idea, but I don't want to make this model more complicated)
6. Pipe roughness coefficient 0.15
7. This model is not good for running in multiple threads, because current section values depend on previous sections values.
8. I haven't included height difference in calculating.
9. If you want to calculate section values as real process you must calculate pipe with length 1 meter for 1/1221 seconds. If you want to use this model to find regulation coefficients you must create balanced coefficients for this regulation coefficients.

Problems:

- I've lost a book with calculation formulas. And I've totaly forgotten how it's called.
- I've not found a good method for calculting branched flow. There's a formula for calculating two branched flow, but there's not formula for calculating three or more branched flow.
You can present three branced flow as two two branched flow, but it gives a bad calculating result.

### [RUS]
Это модель расчета давлений и скоростей потока нефти в трубопроводе.

Общее описание:

Нефтепровод начинается с резервуара имеющего постоянный уровень нефти и выдающего постоянный поток в нефтепровод. 
Нефтепровод разделяется на сечения. В каждом сечений рассчитывается давление и скорость потока на основании давления и скорости
в предыдущей (полученное на текущей итерации) и следующей точке (полученное на прошлой итерации).
Нефтепровод должен заканчиваться резервуаром, иначе давление будет 0. Эквивалентно разрыву трубы

Доступные для расчета объекты:
 - труба
 - заслонка
 - насос
 - обратный клапан
 - резервуар
 

Ограничения
1. Модель справедлива для ламинарного потока нефти
2. Плотность нефти 960 кг/м3
3. Скорость звука в нефти 1221 м/с 
4. Входящий расход нефти постоянный = 10 000 м3/ч
5. Вязкость нефти 300 мм2/с
6. Шероховатость труб 0.15 (средний износ)
7. Модель не очень хороша для многопоточных вычислений, т.к. для вычисления текущей точки нужны значения предыдущей
8. Если вы хотите чтобы модель была эквивалентна реальному процессу необходим коэффициент для масштабирования (волна давления распространяется со скоростью звука, значит 1 метр трубы должен рассчитываться со скоростью 1/1221 секунды). Можно рассчитывать быстрее, но если использовать модель для подбора коэффициентов регулирования, придется вводить дополнительные корректирующие коэффициенты дял их значений.

Теперь о проблемах.
 - Я потерял книгу в которой были приведены формулы для рассчета, и совершенно не помню как она называется. 
 - Я не нашел метода для универсального рассчета давлений и скоростей в разветвлениях. Такая формула существует только для разветвления из 1 в 2.
 Большее количество разветвлений можно собрать из этих элементов, однако это существенно ухудшает точность расчетов.
 
