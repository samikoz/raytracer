package io.raytracer.mechanics.reporters;

public class DefaultReporter implements Reporter {
    private final RenderData data;

    public DefaultReporter(RenderData data) {
        this.data = data;

        System.out.printf("Rendering %d pixel picture, %d remaining%n", data.totalPixelCount, data.blankPixelCount);
    }

    @Override
    public void report(int renderedPixelCount) {
        if (renderedPixelCount % 100 == 0) {
            System.out.print(this.formatMessage(renderedPixelCount));
        }
    }

    protected String formatMessage(int renderedPixelCount) {
        long currentTime = System.nanoTime();
        double timeLeft = (currentTime - this.data.renderStart)*(((double)this.data.blankPixelCount/renderedPixelCount) - 1);
        int minutesElapsed = (int)((currentTime - this.data.renderStart)/(60*Math.pow(10,9)));
        int minutesLeft = (int)(timeLeft/(60*Math.pow(10,9)));
        double progressPercent = (double)(this.data.totalPixelCount - this.data.blankPixelCount + renderedPixelCount)/this.data.totalPixelCount*100;
        return String.format("\r%.2f%% / %d min elapsed / ~%d min left", progressPercent, minutesElapsed, minutesLeft);
    }

    @Override
    public void summarise() {
        long renderEnd = System.nanoTime();
        int totalSeconds = (int)((renderEnd - this.data.renderStart)/Math.pow(10, 9));
        int minutes = totalSeconds / 60;
        int hours = minutes / 60;
        String hoursSummary = hours > 0 ? String.format("%d h, ", hours) : "";
        String minSummary = String.format("%d min, %d sec%n", hours > 0 ? minutes % 60 : minutes, totalSeconds % 60);
        System.out.printf("%nrender took " + hoursSummary + minSummary);
    }
}
