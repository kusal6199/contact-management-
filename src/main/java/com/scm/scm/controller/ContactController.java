package com.scm.scm.controller;

import com.scm.scm.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scm.scm.entities.Contact;
import com.scm.scm.helper.AppConstant;
import com.scm.scm.helper.ContactForm;
import com.scm.scm.helper.CreateContact;
import com.scm.scm.helper.Helper;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.helper.UpdateContact;
import com.scm.scm.services.ContactService;
import com.scm.scm.services.ImageService;
import com.scm.scm.services.UserService;

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
    public String saveData(@Validated(CreateContact.class) @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("errorMessage",
                    Message.builder().content("please correct following errors").messageType(MessageType.red).build());

            return "/user/add_contact";
        }

        try {
            String username = Helper.getEmailOfLoggedInUser(authentication);

            // logger.info("file Info----------------------------------------------------->:
            // {}",
            // contactForm.getContactImage().getOriginalFilename());

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
            // System.out.println(contact);
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
    public String viewContactPage(Model model, Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
        // List<Contact> contactByUserId =
        // contactService.getContactByUserId(user.getUserId());

        Page<Contact> pageContacts = contactService.findByUser(user, page, size, sortBy, direction);

        // System.out.println(contactByUserId);

        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", size);
        return "user/view_contact";
    }

    @PostMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return "redirect:/user/contacts/view";

    }

    @GetMapping("/edit/{id}")
    public String editContactPage(@PathVariable String id, Model model) {

        Contact contact = contactService.getContactById(id);

        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setFavorite(contact.isFavorite());

        model.addAttribute("existingImageUrl", contact.getPicture());
        System.out.println("IMAGE URL = " + contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        return "user/edit_contact";
    }

    @PostMapping("/update/{id}")
    public String updateContact(
            @PathVariable("id") String id,
            @Validated(UpdateContact.class) @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage",
                    Message.builder()
                            .content("Please correct the following errors")
                            .messageType(MessageType.red)
                            .build());
            return "user/edit_contact";
        }

        try {
            Contact contact = contactService.getContactById(id);

            // update basic fields
            contact.setName(contactForm.getName());
            contact.setEmail(contactForm.getEmail());
            contact.setPhoneNumber(contactForm.getPhoneNumber());
            contact.setAddress(contactForm.getAddress());
            contact.setDescription(contactForm.getDescription());
            contact.setLinkedInLink(contactForm.getLinkedInLink());
            contact.setWebsiteLink(contactForm.getWebsiteLink());
            contact.setFavorite(contactForm.isFavorite());

            // ✅ IMAGE UPDATE (SAFE LOGIC)
            if (contactForm.getContactImage() != null &&
                    !contactForm.getContactImage().isEmpty()) {

                // delete old image ONLY if exists
                if (contact.getCloudinaryImagePublicId() != null) {
                    imageService.deleteImage(contact.getCloudinaryImagePublicId());
                }

                String newPublicId = UUID.randomUUID().toString();
                String newImageUrl = imageService.uploadImage(
                        contactForm.getContactImage(),
                        newPublicId);

                contact.setPicture(newImageUrl);
                contact.setCloudinaryImagePublicId(newPublicId);
            }

            contactService.updateContact(contact);

            redirectAttributes.addFlashAttribute("message",
                    Message.builder()
                            .content("Contact updated successfully!")
                            .messageType(MessageType.green)
                            .build());

            return "redirect:/user/contacts/view";

        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error",
                    "Unable to update contact: " + exception.getMessage());
            return "redirect:/user/contacts/view";
        }
    }

    @GetMapping("/search")
    public String searchHandler(@RequestParam("field") String field, @RequestParam("keyword") String value,
            @RequestParam(value = "size", defaultValue = AppConstant.pageSize + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "dirrection", defaultValue = "asc") String dirrection, Model model) {
        logger.info("field {} keyword {}", field, value);

        Page<Contact> pageContact = null;

        if (field.equalsIgnoreCase("Name")) {
            pageContact = contactService.searchByName(value, page, size, sortBy, dirrection);
        } else if (field.equalsIgnoreCase("Email")) {
            pageContact = contactService.searchByEmail(value, page, size, sortBy, dirrection);
        } else if (field.equalsIgnoreCase("Phone")) {
            pageContact = contactService.seachByPhone(value, page, size, sortBy, dirrection);
        }

        logger.info("serach result >------------------------->" + pageContact);
        model.addAttribute("pageContacts", pageContact);

        return "user/search";
    }

}
