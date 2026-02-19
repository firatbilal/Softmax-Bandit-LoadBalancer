import java.util.Random;

public class SoftmaxLoadBalancer implements LoadBalancer {
    private final int k;
    private final double tau;   // Sıcaklık (Temperature) parametresi
    private final double alpha; // Öğrenme oranı / Sabit Adım Boyu (Step-size parameter)

    // Her sunucu için tahmin edilen ortalama gecikme sürelerini tutan dizi (Q-values)
    private final double[] qValues;
    private final Random random;

    public SoftmaxLoadBalancer(int k, double tau, double alpha) {
        this.k = k;
        this.tau = tau;
        this.alpha = alpha;
        this.qValues = new double[k];
        this.random = new Random();

        // Başlangıçta tüm Q değerlerini 0.0 olarak başlatıyoruz.
        // Gecikme pozitif bir değer olduğu için, 0 başlangıcı "Optimistic Initial Values" etkisi yaratır
        // ve ajanın başlarda tüm sunucuları keşfetmesini (exploration) teşvik eder.
        for (int i = 0; i < k; i++) {
            qValues[i] = 0.0;
        }
    }

    @Override
    public int selectServer() {
        double[] preferences = new double[k];
        double maxPreference = Double.NEGATIVE_INFINITY;

        // 1. Adım: Tercih (Preference) değerlerini hesapla.
        // Gecikmeyi MİNİMİZE etmek istediğimiz için qValues değerini eksi (-) olarak alıyoruz.
        for (int i = 0; i < k; i++) {
            preferences[i] = -qValues[i] / tau;

            // Nümerik stabilite için en yüksek tercih değerini buluyoruz.
            if (preferences[i] > maxPreference) {
                maxPreference = preferences[i];
            }
        }

        double[] probabilities = new double[k];
        double sumExp = 0.0;

        // 2. Adım: Softmax formülü ve NÜMERİK STABİLİTE çözümü
        for (int i = 0; i < k; i++) {
            // Klasik e^x yerine e^(x - max_x) kullanıyoruz. (Overflow'u engeller)
            probabilities[i] = Math.exp(preferences[i] - maxPreference);
            sumExp += probabilities[i];
        }

        // 3. Adım: Kümülatif olasılık dağılımı ile rulet tekerleği (Roulette Wheel) seçimi
        double randVal = random.nextDouble() * sumExp;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < k; i++) {
            cumulativeProbability += probabilities[i];
            if (randVal <= cumulativeProbability) {
                return i;
            }
        }

        return k - 1; // Fallback (Matematiksel yuvarlama hatalarına karşı son elemanı dön)
    }

    @Override
    public void update(int serverId, double latency) {
        // Sabit Adım Boyu (Constant Step-Size) Güncelleme Kuralı
        // Q_yeni = Q_eski + alpha * (Hedef - Q_eski)
        // Ortam "non-stationary" olduğu için bu formül, eski verilere üstel olarak azalan bir ağırlık verir.
        qValues[serverId] = qValues[serverId] + alpha * (latency - qValues[serverId]);
    }
}