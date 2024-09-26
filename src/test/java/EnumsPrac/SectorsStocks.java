package EnumsPrac;

public enum SectorsStocks {

    ENERGY("Energy","008"),
    MATERIALS("Materials","009"),
    INDUSTRIALS("Industrials","010");

    private String sectorName;
    private String sectorCode;

    private SectorsStocks(String sectorName, String sectorCode) {
        this.sectorName = sectorName;
        this.sectorCode = sectorCode;
    }

    public String getSectorName() {
        return sectorName;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public static void main(String[] args) {
        for (SectorsStocks sector : SectorsStocks.values()) {
            System.out.println("Sector Name: " + sector.getSectorName()+ ", Code: " + sector.getSectorCode());
        }
    }

}
