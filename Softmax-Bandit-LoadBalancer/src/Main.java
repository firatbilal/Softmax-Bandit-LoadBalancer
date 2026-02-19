import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int K = 5; // Sunucu sayısı (K-kollu canavar problemi)
        int numRequests = 10000; // Simüle edilecek toplam istek sayısı

        // 1. Ortamı (Sunucuları) Hazırla
        List<Server> servers = new ArrayList<>();
        // Sunucuların başlangıçtaki ortalama gecikme süreleri (milisaniye cinsinden)
        double[] initialLatencies = {50.0, 120.0, 75.0, 200.0, 60.0};
        for (int i = 0; i < K; i++) {
            servers.add(new Server(i, initialLatencies[i]));
        }

        // 2. Ajanları (Yük Dengeleyicileri) Başlat
        LoadBalancer randomLB = new RandomLoadBalancer(K);
        LoadBalancer roundRobinLB = new RoundRobinLoadBalancer(K);

        // Softmax için Hiperparametreler:
        // tau (sıcaklık): 5.0 -> Keşif (exploration) ve Sömürü (exploitation) dengesi.
        // alpha (öğrenme oranı): 0.1 -> Non-stationary ortamda geçmişe ne kadar hızlı sünger çekileceği.
        LoadBalancer softmaxLB = new SoftmaxLoadBalancer(K, 5.0, 0.1);

        // Performans ölçümü için toplam gecikmeleri tutacağımız değişkenler
        double totalLatencyRandom = 0;
        double totalLatencyRR = 0;
        double totalLatencySoftmax = 0;

        System.out.println("Simülasyon Başlıyor... Toplam İstek: " + numRequests + "\n");

        // 3. Simülasyon Döngüsü
        for (int step = 0; step < numRequests; step++) {
            // A) Her adımda ortamı (sunucuları) biraz kaydır (Non-stationary drift - Random Walk)
            for (Server server : servers) {
                server.drift();
            }

            // B) Random Ajanı Simülasyonu
            int randomChoice = randomLB.selectServer();
            double randomLatency = servers.get(randomChoice).getLatency();
            randomLB.update(randomChoice, randomLatency);
            totalLatencyRandom += randomLatency;

            // C) Round-Robin Ajanı Simülasyonu
            int rrChoice = roundRobinLB.selectServer();
            double rrLatency = servers.get(rrChoice).getLatency();
            roundRobinLB.update(rrChoice, rrLatency);
            totalLatencyRR += rrLatency;

            // D) Softmax Ajanı Simülasyonu
            int softmaxChoice = softmaxLB.selectServer();
            double softmaxLatency = servers.get(softmaxChoice).getLatency();
            softmaxLB.update(softmaxChoice, softmaxLatency);
            totalLatencySoftmax += softmaxLatency;
        }

        // 4. Sonuçları Analiz Et ve Ekrana Bas
        System.out.println("--- Simülasyon Sonuçları (Ortalama Gecikme - Latency) ---");
        System.out.printf("Random Load Balancer     : %.2f ms\n", (totalLatencyRandom / numRequests));
        System.out.printf("Round-Robin Load Balancer: %.2f ms\n", (totalLatencyRR / numRequests));
        System.out.printf("Softmax Load Balancer    : %.2f ms\n", (totalLatencySoftmax / numRequests));
    }
}