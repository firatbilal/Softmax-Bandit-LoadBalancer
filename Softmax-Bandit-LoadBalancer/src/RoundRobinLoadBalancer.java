public class RoundRobinLoadBalancer implements LoadBalancer {
    private final int k;
    private int currentIndex;

    public RoundRobinLoadBalancer(int k) {
        this.k = k;
        this.currentIndex = 0;
    }

    @Override
    public int selectServer() {
        // Mevcut indeksi seç ve bir sonrakine geç (Modulo işlemi ile başa sarar).
        int selectedServer = currentIndex;
        currentIndex = (currentIndex + 1) % k;
        return selectedServer;
    }

    @Override
    public void update(int serverId, double latency) {
        // Round-Robin deterministik bir algoritmadır, gecikme sürelerini umursamaz.
        // Geri bildirim döngüsü (feedback loop) yoktur.
    }
}