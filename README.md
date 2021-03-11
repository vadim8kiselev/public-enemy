# public-enemy

### Heroku commands
#### Redeployment
heroku scale web=0; 
git commit --allow-empty -m "Trigger Heroku deploy"; 
git push heroku master; 
heroku scale web=1; 
heroku logs --tail;

