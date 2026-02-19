public interface LoadBalancer {
    /**
     * Ajanın K adet sunucu arasından bir seçim yapmasını sağlar.
     * @return Seçilen sunucunun ID'si (index)
     */
    int selectServer();

    /**
     * Seçilen sunucudan dönen gecikme süresine (latency) göre ajanın kendi iç durumunu (state) güncellemesi.
     * @param serverId İstek atılan sunucunun ID'si
     * @param latency Sunucudan dönen yanıt süresi (bizim durumumuzda bu bir 'maliyet' veya ters çevrilmiş 'ödül'dür)
     */
    void update(int serverId, double latency);
}