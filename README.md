Setting Up GitHub
1. Create a GitHub Account
Go to GitHub and sign up.

2. Install Git
Download Git from git-scm.com

Follow installation steps (keep default settings).

3. Configure Git (One-Time Setup)
Open Git Bash (Windows) or Terminal (Mac/Linux) and run:

bash
Copy
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
GitHub Basics: Push, Pull, Commit
Key Terms
Command	Description
git clone	Downloads a repository from GitHub
git add	Stages changes for commit
git commit	Saves changes locally
git push	Uploads changes to GitHub
git pull	Downloads latest changes from GitHub
Workflow Example
Clone a Repository (Download it first)

bash
Copy
git clone https://github.com/username/repository.git
Make changes (Edit files in NetBeans).

Stage changes

bash
Copy
git add .
Commit changes

bash
Copy
git commit -m "Describe your changes here"
Push to GitHub

bash
Copy
git push origin main
Pull latest changes (Before making new edits)

bash
Copy
git pull origin main
Using GitHub in NetBeans
1. Open NetBeans & Set Up Git
Go to Team > Git > Clone

Enter GitHub repo URL & credentials.

2. Commit & Push in NetBeans
✔ Right-click project > Git > Commit (Add a meaningful message!)
✔ Right-click project > Git > Remote > Push

3. Pull Changes
✔ Right-click project > Git > Pull

4. Resolve Conflicts (If Any)
NetBeans will highlight conflicts.

Manually review changes before committing again.

Best Practices & Ethical Collaboration
Do’s
✔ Commit Small, Logical Changes (No huge single commits)
✔ Write Clear Commit Messages (Example: "Fix login bug" instead of "Updated code")
✔ Pull Before Pushing (Avoid conflicts)
✔ Use Branches for New Features (Don’t work directly on main)

Don’ts 
❌ Push Broken Code (Test before pushing)
❌ Delete Others' Code Without Discussion
❌ Force Push (git push -f) (Can erase others' work)
