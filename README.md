# roboshop-jenkins

gh api /users/USERNAME


gh api \
--method PUT \
-H "Accept: application/vnd.github+json" \
-H "X-GitHub-Api-Version: 2022-11-28" \
/repos/raghudevopsb79/roboshop-catalogue/environments/qa -f "reviewers[][type]=User" -F "reviewers[][id]=12345"

