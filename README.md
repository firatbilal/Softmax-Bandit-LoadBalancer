# Softmax-Bandit-LoadBalancer

Bu proje, zamanla değişen (non-stationary) ve gürültülü (noisy) bir ağ ortamında, K adet sunucuya gelen istekleri en düşük gecikme (latency) süresiyle dağıtmayı amaçlayan istemci taraflı (client-side) bir yük dengeleyici (load balancer) simülasyonudur. 

Klasik yük dengeleme algoritmalarının yetersiz kaldığı bu dinamik ortamda problem, bir **K-Armed Bandit (Çok Kollu Canavar)** problemi olarak modellenmiş ve çözüm olarak pekiştirmeli öğrenme tabanlı **Softmax Action Selection** algoritması implemente edilmiştir.

Proje, Agentic Kodlama (Agentic AI Workflow) prensipleri kullanılarak modüler ve nesne yönelimli bir mimaride (OOP) Java ile geliştirilmiştir.

## 🚀 Özellikler & Çözülen Temel Problemler

* **Non-Stationary Environment (Zamanla Değişen Ortam):** Sunucuların yanıt süreleri statik değildir. İstatistiksel *Random Walk (Rastgele Yürüyüş)* yöntemi ile sunucu performanslarının zaman içindeki kaymaları (drift) simüle edilmiştir.
* **Noisy Rewards (Gürültülü Ödüller):** Her isteğe dönen yanıt süresine, ağ dalgalanmalarını simüle eden bir Gauss gürültüsü (Gaussian Noise) eklenmiştir.
* **Sabit Adım Boyu (Constant Step-Size):** Softmax ajanı, ortamın dinamik yapısına adapte olabilmek için geçmişteki tüm verilerin ortalamasını almak yerine, sabit bir öğrenme oranı ($\alpha$) kullanarak en güncel gecikme sürelerine daha yüksek ağırlık verir.
* **Nümerik Stabilite (Numerical Stability):** Softmax fonksiyonundaki üstel hesaplamaların ($e^x$) büyük sayılarda taşma (overflow) veya $NaN$ hatası üretmesini engellemek için **Max-Subtraction (Maksimum Çıkarma)** tekniği uygulanmıştır.

## 🏗️ Sistem Mimarisi ve Bileşenler

Proje, birbirine gevşek bağlı (loosely coupled) aşağıdaki sınıflardan oluşmaktadır:

1. **`Server`:** Zamanla değişen ve gürültülü gecikme süreleri üreten sunucu simülasyonu.
2. **`LoadBalancer` (Interface):** Tüm ajanların uyması gereken temel seçim ve geri bildirim (update) şablonu.
3. **`RandomLoadBalancer` (Baseline):** Geçmiş veriyi göz ardı ederek $O(1)$ zamanda rastgele sunucu seçen ajan.
4. **`RoundRobinLoadBalancer` (Baseline):** Geri bildirim (feedback loop) içermeyen, istekleri sırayla dağıtan klasik algoritma.
5. **`SoftmaxLoadBalancer` (Core):** Kümülatif olasılık dağılımı (Roulette Wheel Selection) ve Boltzmann dağılımını kullanarak keşif-sömürü (exploration-exploitation) dengesini kuran zeki ajan.
6. **`Main`:** $10.000$ isteklik simülasyonu koşturan ve algoritmaların ortalama gecikme performanslarını karşılaştıran orkestratör sınıf.

## 📊 Çalışma Zamanı (Time Complexity) Analizi

* **Random & Round-Robin:** Sunucu seçimi $O(1)$ zamanda gerçekleşir.
* **Softmax:** Tercihlerin güncellenmesi $O(1)$ iken, seçim aşamasında olasılıkların hesaplanıp kümülatif toplam üzerinden karar verilmesi $K$ adet sunucu için $O(K)$ zaman karmaşıklığına sahiptir. Dağıtık sistemlerde sunucu sayısı genellikle sınırlı olduğu için bu doğrusal zaman, sistemde bir darboğaz (bottleneck) yaratmaz.

## 🛠️ Kurulum ve Çalıştırma

Projeyi yerel makinenizde çalıştırmak için sisteminizde Java (JDK 8 veya üzeri) kurulu olmalıdır.

1. Repoyu klonlayın:
   ```bash
   git clone [https://github.com/KULLANICI_ADINIZ/Softmax-Bandit-LoadBalancer.git](https://github.com/KULLANICI_ADINIZ/Softmax-Bandit-LoadBalancer.git)
2. Proje dizinine gidin ve Java dosyalarını derleyin:
   ```bash
   javac *.java
3. Simülasyonu başlatın:
   ```bash
   java Main
