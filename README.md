# PollyCall
This is a project that clones the functionality of the Whoscall including frontend and backend. It can identify the caller's identity when the phone is ringing.
User can purchase the premium service by Google Billing Library.
In this project, I execute the backend in the AWS EC2 instance and MySQL with the Django framework.
![img.png](img.png)

## Use Path

### Upload the strange number to the server
<img width="743" alt="截圖 2024-01-28 上午9 51 44" src="https://github.com/pollyannagogolook/PollyCall/assets/155519546/c904edf5-406f-45bc-87dc-370454cc30e8">

### get information of the strange number from the server when the phone is ringing
<img width="630" alt="screenshot_polly_call.png" src="https://github.com/pollyannagogolook/PollyCall/assets/155519546/d881800f-1786-487f-b56d-45a04494bed4">

## Architecture
![截圖 2024-04-15 上午10.36.16.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fbm%2Fjn8r1j857pgcyqwp_pyxwk400000gq%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_h5epNq%2F%E6%88%AA%E5%9C%96%202024-04-15%20%E4%B8%8A%E5%8D%8810.36.16.png)


## Features
- [x] Identify the caller's identity when the phone is ringing
- [x] Local database to store the caller's identity
- [x] Notifications to display the caller's identity
- [x] UI to display the form to report the caller's identity




## Skills
- CallScreeningService
- ForegroundService
- BillingClient
- Room
- MVVM
- Retrofit, Moshi
- MVVM
- Hilt
- Python (Django)


## API
Because the API is not stable, before test this app, please test the api in Postman first to make sure the api is working.

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
