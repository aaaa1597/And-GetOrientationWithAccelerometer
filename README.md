# And-GetOrientationWithAccelerometer
加速度センサーから回転をとるサンプル

AndroidManifest.xmlに、android:screenOrientation="portrait"の設定をすると、
onConfigurationChanged()のイベントが発生せず、スマホの向きが取れない。
なので、自力で、スマホの向きを取得するサンプルを作ってみた。

画像は[illust image](https://illustimage.com/) ©dak 様のを使わせていもらいました。
![rabit](https://user-images.githubusercontent.com/27885482/218999940-64ae96c2-5878-4244-9771-ae0edd1892f9.png)
ありがとうござます。
