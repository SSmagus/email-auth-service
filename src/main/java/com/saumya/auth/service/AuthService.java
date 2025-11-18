package com.saumya.auth.service;

import com.saumya.auth.config.JwtUtil;
import com.saumya.auth.dto.RegisterRequest;
import com.saumya.auth.dto.LoginRequest;
import com.saumya.auth.dto.ResetPasswordRequest;
import com.saumya.auth.entity.User;
import com.saumya.auth.entity.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request, String verifyLinkBase) {
        User user = userService.register(request.getEmail(), request.getPassword());
        VerificationToken token = tokenService.createToken(user);
        String link = verifyLinkBase + token.getToken();
        String html = "<p>Click the link to verify your account:</p><a href=\"" + link + "\">Verify</a>";
        emailService.sendHtml(user.getEmail(), "Verify your account", html);
    }

    public String login(LoginRequest request) {
        User user = userService.getByEmail(request.getEmail()).orElse(null);
        if (user == null) return null;
        if (!user.isVerified()) return null;
        if (!userService.checkPassword(user, request.getPassword())) return null;
        return jwtUtil.generateToken(user.getEmail());
    }

    public boolean verifyAccount(String tokenValue) {
        VerificationToken token = tokenService.verifyToken(tokenValue);
        if (token == null) return false;
        userService.markVerified(token.getUser());
        tokenService.markUsed(token);
        return true;
    }

    public void requestPasswordReset(String email, String resetLinkBase) {
        User user = userService.getByEmail(email).orElse(null);
        if (user == null) return;
        VerificationToken token = tokenService.createToken(user);
        String link = resetLinkBase + token.getToken();
        String html = "<p>Reset your password:</p><a href=\"" + link + "\">Reset Password</a>";
        emailService.sendHtml(email, "Reset Password", html);
    }

    public boolean resetPassword(ResetPasswordRequest request) {
        VerificationToken token = tokenService.verifyToken(request.getToken());
        if (token == null) return false;
        userService.updatePassword(token.getUser(), request.getNewPassword());
        tokenService.markUsed(token);
        return true;
    }
}
