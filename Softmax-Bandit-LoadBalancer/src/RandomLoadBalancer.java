import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final int k; // Toplam sunucu sayısı (K-armed bandit'teki K)
    private final Random random;

    public RandomLoadBalancer(int k) {
        this.k = k;
        this.random = new Random();
    }

    @Override
    public int selectServer() {
        // 0 ile K-1 arasında rastgele, uniform (eşdağılımlı) bir sunucu seçer.
        return random.nextInt(k);
    }

    @Override
    public void update(int serverId, double latency) {
        // Random algoritması geçmişten ders almaz (No learning/tracking).
        // Bu yüzden update metodu boş bırakılmıştır.
    }
}