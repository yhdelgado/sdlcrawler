/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.gws.sdlcrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Yusniel Hidalgo Delgado [yhidalgo86 at gmail dot com]
 */
public class PdfCrawlerConfigManager {

    private String path;

    private PdfCrawlerConfigManager(String configPath) {
        path = configPath;
    }

    public String getPath() {
        return path;
    }

    public static PdfCrawlerConfigManager getInstance(String path) {
        PdfCrawlerConfigManager cm;
        cm = new PdfCrawlerConfigManager(path);
        return cm;
    }

    public static PdfCrawlerConfigManager getInstance() {
        PdfCrawlerConfigManager cm;
        return cm = new PdfCrawlerConfigManager("config/sdlcrawler.conf");
    }

    public void generateDefaultConfigFile() throws IOException {
        Properties configOpts = new Properties();
        //configOpts.setProperty("sdlcrawler.ProxyDomain", "example.com");
        configOpts.setProperty("sdlcrawler.ProxyHost", "localhost");
        configOpts.setProperty("sdlcrawler.ProxyPort", "3128");
        configOpts.setProperty("sdlcrawler.ProxyUser", "myuser");
        configOpts.setProperty("sdlcrawler.ProxyPass", "mypass");
        configOpts.setProperty("sdlcrawler.Redirects", "true");
        configOpts.setProperty("sdlcrawler.MaxDownloadSize", "5242880");
        configOpts.setProperty("sdlcrawler.MaxOutgoingLinks", "5000");
        configOpts.setProperty("sdlcrawler.MaxTotalConnections", "100");
        configOpts.setProperty("sdlcrawler.MaxConnectionsPerHost", "100");
        configOpts.setProperty("sdlcrawler.IncludeBinaryContent", "true");
        configOpts.setProperty("sdlcrawler.IncludeHttpsPages", "true");
        configOpts.setProperty("sdlcrawler.UserAgent", "sdlcrawler");
        configOpts.setProperty("sdlcrawler.MaxPagesToFetch", "-1");
        configOpts.setProperty("sdlcrawler.MaxDepthCrawl", "4");
        configOpts.setProperty("sdlcrawler.ResumableCrawling", "false");
        configOpts.setProperty("sdlcrawler.CrawlStorageFolder", "root");
        configOpts.setProperty("sdlcrawler.CrawlPdfFolder", "pdf");
        configOpts.setProperty("sdlcrawler.NumberOfCrawlers", "5");
        configOpts.setProperty("sdlcrawler.SeedFile", "urls");
        configOpts.setProperty("sdlcrawler.SocketTimeout", "0");
        configOpts.setProperty("sdlcrawler.PolitenessDelay", "200");
        configOpts.setProperty("sdlcrawler.ConnectionTimeout", "30000");
        configOpts.store(new FileOutputStream(new File(getPath())), "Configuration Options");
    }

    public Properties loadConfigFile() throws IOException {
        Properties loadedProperties = new Properties();
        File file = new File(this.getPath());
        if (file.exists()) {
            loadedProperties.load(new FileInputStream(file));
        } else {
            generateDefaultConfigFile();
            loadedProperties.load(new FileInputStream(file));
        }
        return loadedProperties;
    }
}
