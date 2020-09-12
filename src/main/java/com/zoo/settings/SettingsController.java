package com.zoo.settings;

import com.zoo.account.AccountService;
import com.zoo.account.CurrentUser;
import com.zoo.domain.Account;
import com.zoo.settings.Validator.PasswordValidator;
import com.zoo.settings.Validator.ProfileValidator;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
@RequiredArgsConstructor
@Controller
public class SettingsController {
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final PasswordValidator passwordValidator;

    static final String SETTINGS_PROFILE = "/settings/profile";
    static final String SETTINGS_PASSWORD = "/settings/password";
    static final String SETTINGS_ALARM = "/settings/alarm";
    static final String SETTINGS_FAVORITE_ANIMAL = "/settings/animal";
    static final String SETTINGS_ACCOUNT = "/settings/account";
    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){webDataBinder.addValidators(passwordValidator);}


    @GetMapping(SETTINGS_PROFILE)
    public String settingsProfile(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        ProfileForm profileForm = modelMapper.map(account, ProfileForm.class);
        model.addAttribute(profileForm);
        return SETTINGS_PROFILE;
    }

    @PostMapping(SETTINGS_PROFILE)
    public String settingsProfile(@CurrentUser Account account, @Valid ProfileForm profileForm, Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute("message","프로필 정보 변경에 실패했습니다.");
            return SETTINGS_PROFILE;
        }
        accountService.updateProfile(account, profileForm);
        attributes.addFlashAttribute("message","프로필 설정이 변경되었습니다.");
        return "redirect:"+SETTINGS_PROFILE;
    }

    @GetMapping(SETTINGS_PASSWORD)
    public String settingsPassword(@CurrentUser Account account, Model model){
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute(account);
        return SETTINGS_PASSWORD;
    }
    @PostMapping(SETTINGS_PASSWORD)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute("message","비밀번호 변경이 되지 않았습니다.");
            return SETTINGS_PASSWORD;
        }
        accountService.updatePassword(account, passwordForm);
        attributes.addFlashAttribute("message","비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:"+ SETTINGS_PASSWORD;
    }
}