package com.user.steammgmt.service;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NavigationService {

	public String resolveRedirectURL(HttpSession session, String redirectSessionURL, List<String> notAccessURLs,
			String defaultURL) {
		String redirectURL = (String) session.getAttribute(redirectSessionURL);
		if (redirectURL != null) {
			session.removeAttribute(redirectSessionURL);
			boolean isAccessURL = true;
			for (String notAccessURL : notAccessURLs) {
				if (redirectURL.contains(notAccessURL)) {
					isAccessURL = false;
					break;
				}
			}
			if (isAccessURL) {
				return "redirect:" + redirectURL;
			}
			return "redirect:" + defaultURL;
		}
		return "redirect:" + defaultURL;
	}

	public void saveURL(HttpSession session, String sessionURL, String URL) {
		session.setAttribute(sessionURL, URL);
	}

}
