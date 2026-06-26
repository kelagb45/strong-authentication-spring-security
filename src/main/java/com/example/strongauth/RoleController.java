package com.example.strongauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#0f2027,#203a43,#2c5364);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:45px;border-radius:22px;width:520px;text-align:center;box-shadow:0 12px 30px rgba(0,0,0,0.3);">
                        <h1 style="color:#203a43;">Admin Dashboard</h1>
                        <p style="font-size:18px;color:#555;">Welcome, ADMIN. You have full administrative access.</p>

                        <div style="margin-top:25px;text-align:left;background:#f4f7f9;padding:20px;border-radius:12px;">
                            <p>✅ Manage users</p>
                            <p>✅ View system security settings</p>
                            <p>✅ Access protected admin resources</p>
                        </div>

                        <form action="/logout" method="post" style="margin-top:25px;">
                            <button style="padding:12px 25px;background:#203a43;color:white;border:none;border-radius:8px;cursor:pointer;font-size:15px;">
                                Logout
                            </button>
                        </form>
                    </div>
                </body>
                </html>
                """;
    }

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return """
                <html>
                <body style="margin:0;font-family:Segoe UI;background:linear-gradient(135deg,#667eea,#764ba2);display:flex;justify-content:center;align-items:center;height:100vh;">
                    <div style="background:white;padding:45px;border-radius:22px;width:520px;text-align:center;box-shadow:0 12px 30px rgba(0,0,0,0.3);">
                        <h1 style="color:#667eea;">User Dashboard</h1>
                        <p style="font-size:18px;color:#555;">Welcome, USER. You have standard user access.</p>

                        <div style="margin-top:25px;text-align:left;background:#f6f3ff;padding:20px;border-radius:12px;">
                            <p>✅ View personal profile</p>
                            <p>✅ Access user services</p>
                            <p>✅ Use protected user resources</p>
                        </div>

                        <form action="/logout" method="post" style="margin-top:25px;">
                            <button style="padding:12px 25px;background:#667eea;color:white;border:none;border-radius:8px;cursor:pointer;font-size:15px;">
                                Logout
                            </button>
                        </form>
                    </div>
                </body>
                </html>
                """;
    }
}