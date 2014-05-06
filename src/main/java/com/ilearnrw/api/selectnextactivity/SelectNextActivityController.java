package com.ilearnrw.api.selectnextactivity;

import java.util.ArrayList;
import java.util.List;

import ilearnrw.user.profile.UserProfile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ilearnrw.api.datalogger.services.CubeService;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class SelectNextActivityController {

	private static Logger LOG = Logger
			.getLogger(SelectNextActivityController.class);

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	UserService userService;

	@Autowired
	CubeService cubeService;

	@RequestMapping(value = "/activity/next", method = RequestMethod.GET)
	public @ResponseBody
	List<NextActivities> selectNextActivity() {
		
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String username = auth.getName();
		LOG.debug(String.format("Retrieving next activity for user %s", username));
		User loggedInUser = userService.getUserByUsername(username);
		try {
			UserProfile profile = profileProvider.getProfile(loggedInUser.getId());
			List<NextActivities> dummy = new ArrayList<NextActivities>();
			NextActivities dummy1 = new NextActivities();
			dummy1.setProblem(3, 1);
			dummy1.addActivity("Whack a Mole");
			dummy.add(dummy1);
			dummy1 = new NextActivities();
			dummy1.setProblem(3, 3);
			dummy1.addActivity("Whack a Mole");
			dummy1.addActivity("more apps here");
			dummy.add(dummy1);
			return dummy;

		} catch (ProfileProviderException e) {
			e.printStackTrace();
		}

		return null;
	}
}
