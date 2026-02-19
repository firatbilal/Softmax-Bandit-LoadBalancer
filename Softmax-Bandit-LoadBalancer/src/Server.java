import java.util.Random;

public class Server {
    private final int id;
    private double trueMeanLatency; // Sunucunun o anki "gerçek" ortalama gecikme süresi
    private final Random random;

    public Server(int id, double initialMeanLatency) {
        this.id = id;
        this.trueMeanLatency = initialMeanLatency;
        this.random = new Random();
    }

    /**
     * İstemci bu sunucuya bir istek (request) gönderdiğinde dönen yanıt süresi.
     * Ortam "noisy" (gürültülü) olduğu için gerçek ortalamaya Gauss gürültüsü eklenir.
     */
    public double getLatency() {
        // Standart sapması 10ms olan bir Normal (Gauss) dağılımı ile gürültü ekliyoruz.
        double noise = random.nextGaussian() * 10;
        double actualLatency = trueMeanLatency + noise;

        // Gecikme süresi fiziksel olarak 0'dan küçük olamaz, alt sınır koyuyoruz.
        return Math.max(1.0, actualLatency);
    }

    /**
     * Ortamın "Non-stationary" (zamanla değişen) doğasını simüle eden metot.
     * Her zaman adımında (time step) sunucunun performansında "Random Walk" (rastgele yürüyüş) yaşanır.
     */
    public void drift() {
        // Ortalama gecikme süresine her adımda küçük bir kayma (drift) eklenir.
        // Standart sapması 2ms olan bir kayma simüle ediyoruz.
        double driftAmount = random.nextGaussian() * 2;
        trueMeanLatency += driftAmount;

        // Ortalama gecikme süresinin çok gerçek dışı (negatif vs.) seviyelere düşmesini engelliyoruz.
        trueMeanLatency = Math.max(10.0, trueMeanLatency);
    }

    public int getId() {
        return id;
    }

    public double getTrueMeanLatency() {
        return trueMeanLatency;
    }
}