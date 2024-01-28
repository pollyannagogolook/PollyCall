# PollyCall
This is a project that clones the functionality of the Whoscall. It supposed to be able to identify the caller's identity when the phone is ringing. The project is still under development.
Currently, user can upload the strange number successfully. However, the app is not able to get the information of the strange number from the server when the phone is ringing.


## Use Path

### Upload the strange number to the server
<img width="743" alt="截圖 2024-01-28 上午9 51 44" src="https://github.com/pollyannagogolook/PollyCall/assets/155519546/c904edf5-406f-45bc-87dc-370454cc30e8">

### get information of the strange number from the server when the phone is ringing
<img width="629" alt="screenshot_polly_call.png" src="https://github.com/pollyannagogolook/PollyCall/assets/155519546/f809d95e-5e99-4f84-9af7-7005b2fb55a2">



## Features
- [x] Identify the caller's identity when the phone is ringing
- [x] Local database to store the caller's identity
- [x] Notifications to display the caller's identity
- [x] UI to display the form to report the caller's identity




## Skills
- CallScreeningService
- ForegroundService
- Room
- MVVM
- Retrofit, Moshi
- MVVM
- Hilt
- Python (Django)


## API
Because the API is not stable, before test this app, please call the api first to make sure the api is working.

### Get the information of the strange number
`GET http://3.25.150.64:8000/catalog/number/all/`

### Upload the strange number
`POST http://3.25.150.64:8000/catalog/number/create/`
payload: 
```
{
    "number": "0912345678",
    "owner": "test",
    "is_scam": false,
}
```

## Installation

### Clone the project
`git clone https://github.com/pollyannagogolook/PollyCall`

## Author
This project is created by Pollyanna Wu in Gogolook. 
