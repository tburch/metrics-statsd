package com.bealetech.metrics.reporting;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.metrics.BaseReporterFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * A factory for {@link com.bealetech.metrics.reporting.StatsdReporter} instances.
 * <p/>
 * <b>Configuration Parameters:</b>
 * <table>
 *     <tr>
 *         <td>Name</td>
 *         <td>Default</td>
 *         <td>Description</td>
 *     </tr>
 *     <tr>
 *         <td>host</td>
 *         <td>localhost</td>
 *         <td>The hostname of the Statsd server to report to.</td>
 *     </tr>
 *     <tr>
 *         <td>port</td>
 *         <td>8080</td>
 *         <td>The port of the Statsd server to report to.</td>
 *     </tr>
 *     <tr>
 *         <td>prefix</td>
 *         <td><i>None</i></td>
 *         <td>The prefix for Metric key names to report to Statsd.</td>
 *     </tr>
 * </table>
 */
@JsonTypeName("statsd")
public class StatsdReporterFactory extends BaseReporterFactory {
    @NotEmpty
    private String host = "localhost";

    @Range(min = 0, max = 49151)
    private int port = 8080;

    @NotNull
    private String prefix = "";

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public String getPrefix() {
        return prefix;
    }

    @JsonProperty
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Configures and builds a {@link com.codahale.metrics.ScheduledReporter} instance for the given registry.
     *
     * @param registry the metrics registry to report metrics from.
     * @return a reporter configured for the given metrics registry.
     */
    @Override
    public ScheduledReporter build(MetricRegistry registry) {
        Statsd statsd = new Statsd(host, port);
        return StatsdReporter.forRegistry(registry)
                .prefixedWith(prefix)
                .convertDurationsTo(getDurationUnit())
                .convertRatesTo(getRateUnit())
                .filter(getFilter())
                .build(statsd);
    }
}