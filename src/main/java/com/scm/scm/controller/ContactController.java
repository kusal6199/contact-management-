package com.scm.scm.controller;

import com.scm.scm.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scm.scm.entities.Contact;

import com.scm.scm.forms.ContactForm;
import com.scm.scm.helper.Helper;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.services.ContactService;
import com.scm.scm.services.ImageService;
import com.scm.scm.services.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    // controller to return add contact page

    @GetMapping("/add")
    public String addContactViewPage(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveData(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        MultipartFile file = contactForm.getContactImage();
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 2 * 1024 * 1024) {
                bindingResult.rejectValue(
                        "contactImage",
                        "file.size",
                        "File size should be less than 2MB");
            }

            String contentType = file.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                bindingResult.rejectValue(
                        "contactImage",
                        "file.type",
                        "Only image files are allowed");
            }

        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("errorMessage",
                    Message.builder().content("please correct following errors").messageType(MessageType.red).build());

            return "/user/add_contact";
        }

        try {
            String username = Helper.getEmailOfLoggedInUser(authentication);

            logger.info("file Info----------------------------------------------------->: {}",
                    contactForm.getContactImage().getOriginalFilename());

            String fileName = UUID.randomUUID().toString();

            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());

            contact.setAddress(contactForm.getAddress());
            contact.setEmail(contactForm.getEmail());
            contact.setFavorite(contactForm.isFavorite());
            contact.setDescription(contactForm.getDescription());
            contact.setLinkedInLink(contactForm.getLinkedInLink());
            contact.setPhoneNumber(contactForm.getPhoneNumber());
            contact.setName(contactForm.getName());

            contact.setUser(userService.getUserByEmail(username));
            contact.setWebsiteLink(contactForm.getWebsiteLink());
            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
            // contact.setFavorite(true);

            contactService.saveContact(contact);
            System.out.println(contact);
            redirectAttributes.addFlashAttribute("message",
                    Message.builder().content("contact added success!").messageType(MessageType.green).build());
            return "redirect:/user/contacts/add";
        } catch (Exception exception) {
            model.addAttribute("error", "Error saving contact: " + exception.getMessage());
            return "user/add_contact";
        }
    }

    // contact view page

    @GetMapping("/view")
    public String viewContactPage(Model model, Authentication authentication) {

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        List<Contact> contactByUserId = contactService.getContactByUserId(user.getUserId());
        // System.out.println(contactByUserId);
        model.addAttribute("contacts", contactByUserId);
        return "user/view_contact";
    }

    @DeleteMapping("/delete")
    public String deleteContact() {

        return "redirect:user/view_contact";

    }

}
