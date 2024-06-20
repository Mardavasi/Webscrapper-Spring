package com.marlon.webscrapper.Service;

import com.marlon.webscrapper.models.Webpage;
import com.marlon.webscrapper.repository.WebpageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class WebscrapperService {
    @Autowired
    private WebpageRepository repository;

    public void scrapeAndSave(String url) throws IOException {
        Document document  = Jsoup.connect(url).get();

        String domain = getDomainFromUrl(url);
        String title = document.title();
        String description = document.select("meta[name=description]")
                .attr("content");
        String picture = document.select("meta[property=og:image]")
                .attr("content");

        Webpage webpage = new Webpage();
        webpage.setTitle(title);
        webpage.setDescription(description);
        webpage.setPicture(picture);
        webpage.setDomain(domain);
        webpage.setUrl(url);

        repository.save(webpage);
    }
    private String getDomainFromUrl(String url) {
        String domain = url.replaceFirst("^(https?://)?(www\\.)?", "");
        int index = domain.indexOf('/');
        if (index != -1) {
            domain = domain.substring(0, index);
        }
        return domain;
    }

}
