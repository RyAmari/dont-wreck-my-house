# dont-wreck-my-house
MasteryProject

Mastery Project Test Process
# First
- test view reservations method (option 1)
- make sure host is found and proper reservations for that specific host are displayed
- if host with no reservations is inputted, no reservations found
# Second 
- test make reservation method (option 2)
- user selects host by entering email
- user also selects guest by entering email
- user enters dates both start and end
- method validates dates to make sure there are no conflicts with existing reservations
- if sucessful, adds reservation to proper reservations file with proper reservation info displayed
# Third 
- Test edit method(option 3)
- aware of issue where entering overlapping dates of the reservation being updated causes conflict
- could not figure out how to prevent this specific issue as of now
- otherwise, edit method accepts input, and changes the already existing reservation's start and end dates successfully

# Fourth 
- Cancel reservation method (option 4)
- method properly displays reservations of user selected host 
- only displays the reservations that are in the future to prevent 
- possibility of user attempting to cancel a past reservation
- upon completion of prompts, method properly deletes selected reservation from the correct file containing the reservation

Mastery Project Plan

# Day One
- Finish plan with as much detail as possible
- Create Schedule of task
- Design Docs(Unclear on what this means right now)\

# Day Two
- Get all packages and models defined and set up
- Start from the data layer and work forward from there
- should be able to complete this by end of tuesday,
- if not the entire data layer then a good portion of it

# Day Three
- Finish up data layer if not already done
- start on the domain layer
- progress presentation

# Day Four
- Day fully dedicated to domain layer building and testing
- if possible, begin UI layer
- would like to get a better understanding of effective testing

# Day Five
- Begin UI layer to work over into the weekend, preferably 
- get it built by eod friday so weekend can be dedicated toward 
- testing and spring configuration
- progress presentation

# Weekend
- Finish final layer (UI) if not already done
- Hunt for bugs/fix them, write test targeted toward them
- refine project