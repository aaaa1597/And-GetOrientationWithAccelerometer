# And-GetOrientationWithAccelerometer
加速度センサーから回転をとるサンプル

AndroidManifest.xmlに、android:screenOrientation="portrait"の設定をすると、
onConfigurationChanged()のイベントが発生せず、スマホの向きが取れない。
なので、自力で、スマホの向きを取得するサンプルを作ってみた。
