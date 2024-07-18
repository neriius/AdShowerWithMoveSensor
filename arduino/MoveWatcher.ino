const int trigPin = 9;
const int echoPin = 10;

#define HC_TRIG 9
#define HC_ECHO 10

void setup() {
  Serial.begin(9600);       // для связи
  pinMode(HC_TRIG, OUTPUT); // trig выход
  pinMode(HC_ECHO, INPUT);  // echo вход
}
void loop() {
  float dist = getDist();   // получаем расстояние

  if(dist < 200){
    Serial.println(1);
  }else{
    Serial.println(0);
  }
  delay(50);
}
// сделаем функцию для удобства
float getDist() {
  // импульс 10 мкс
  digitalWrite(HC_TRIG, HIGH);
  delayMicroseconds(10);
  digitalWrite(HC_TRIG, LOW);
  // измеряем время ответного импульса
  uint32_t us = pulseIn(HC_ECHO, HIGH);
  // считаем расстояние и возвращаем
  return (us / 58.2);
}
