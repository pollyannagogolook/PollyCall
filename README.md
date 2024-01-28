# PollyCall
This is a project that clones the functionality of the Whoscall. It supposed to be able to identify the caller's identity when the phone is ringing. The project is still under development.
Currently, user can upload the strange number successfully. However, the app is not able to get the information of the strange number from the server when the phone is ringing.


## Use Path

### Upload the strange number to the server
![upload_strange_number.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fbm%2Fjn8r1j857pgcyqwp_pyxwk400000gq%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_8GBHyh%2F%E6%88%AA%E5%9C%96%202024-01-28%20%E5%87%8C%E6%99%A81.05.53.png)

### get information of the strange number from the server when the phone is ringing
![get_number_info.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fbm%2Fjn8r1j857pgcyqwp_pyxwk400000gq%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_TSg5wy%2F%E6%88%AA%E5%9C%96%202024-01-28%20%E4%B8%8A%E5%8D%889.32.13.png)



## Features
- [x] Identify the caller's identity when the phone is ringing
- [x] Local database to store the caller's identity
- [x] Notifications to display the caller's identity
- [x] UI to display the form to report the caller's identity

![screenshot_polly_call.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fbm%2Fjn8r1j857pgcyqwp_pyxwk400000gq%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_4KeRR8%2F%E6%88%AA%E5%9C%96%202024-01-28%20%E5%87%8C%E6%99%A812.04.57.png)

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
GET http://3.25.150.64:8000/catalog/number/all/

### Upload the strange number
POST http://3.25.150.64:8000/catalog/number/create/
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


```