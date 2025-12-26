package com.user.steammgmt.service;

import com.user.steammgmt.model.Game;
import com.user.steammgmt.model.Publisher;
import com.user.steammgmt.model.Record;
import com.user.steammgmt.repository.PublisherRepository;
import com.user.steammgmt.repository.RecordRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class PublisherService {

	private final RecordRepository recordRepository;
	private final PublisherRepository publisherRepository;
	private final GameService gameService;

	public PublisherService(RecordRepository recordRepository, PublisherRepository publisherRepository,
			GameService gameService) {
		this.recordRepository = recordRepository;
		this.publisherRepository = publisherRepository;
		this.gameService = gameService;
	}

	// Lấy danh sách tất cả các nhà phát hành
	public List<Publisher> getAllPublishers() {
		return publisherRepository.findAll();
	}

	public List<Publisher> getPublishersWithDetails() {
		List<Publisher> publishers = getAllPublishers();
		for (Publisher publisher : publishers) {
			List<Game> games = gameService.getGamesByPublisherId(publisher.getPublisherId());
			publisher.setProductQuantity(games.size());
			Game featuredGame = games.stream().max(Comparator.comparingInt(Game::getPeakPlayers)).orElse(null);
			publisher.setFeaturedGame(featuredGame);
		}
		return publishers;
	}

	public Publisher getPublisherWithDetails(String publisherId) {
		Publisher publisher = getPublisherById(publisherId);
		List<Game> games = gameService.getGamesByPublisherId(publisherId);
		publisher.setProductQuantity(games.size());
		return publisher;
	}

	// Lưu hoặc cập nhật một nhà phát hành
	public void addPublisher(Publisher publisher) {
		publisherRepository.save(publisher);
		recordRepository.save(new Record("Publisher", String.valueOf(publisher.getPublisherId()), "Add", new Date()));
	}

	public void updatePublisher(String publisherId, Publisher updatedPublisher) {
		Publisher existingPublisher = getPublisherById(publisherId);
		existingPublisher.setPublisherName(updatedPublisher.getPublisherName());
		existingPublisher.setLinkWebsite(updatedPublisher.getLinkWebsite());
		existingPublisher.setPublisherInfo(updatedPublisher.getPublisherInfo());
		existingPublisher.setPublisherImage(updatedPublisher.getPublisherImage());
		publisherRepository.save(existingPublisher);
		recordRepository.save(new Record("Publisher", String.valueOf(publisherId), "Update", new Date()));
	}

	// Lấy nhà phát hành theo ID
	public Publisher getPublisherById(String publisherId) {
		return publisherRepository.findById(publisherId).orElseThrow();
	}

	// Xóa nhà phát hành theo ID
	public void deletePublisher(String publisherId) {
		publisherRepository.deleteById(publisherId);
		recordRepository.save(new Record("Publisher", String.valueOf(publisherId), "Delete", new Date()));
	}
}
