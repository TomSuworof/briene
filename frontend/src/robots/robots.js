import robotstxt from "generate-robotstxt";

robotstxt({
    policy: [
        {
            userAgent: "*",
            allow: [
                "/articles/",
                "/author/",
                "/tags/"
            ],
            disallow: [
                "/api/",
                "/terms_of_use",
                "/login",
                "/register",
                "/password_reset",
                "/profile",
                "/search",
                "/admin"
            ]
        },
    ]
})
    .then((content) => {
        console.log(content);
        return content;
    })
    .catch((error) => {
        throw error;
    });