import requests
import requests_toolbelt.utils

def get_cookies(cookies):
    rCookies = {}
    for s in cookies.split(";"):
        c = s.split("=")[0]
        v = s.split("=")[1]
        rCookies[c] = v
    return rCookies

def first_request(url, cookies):
    rCookies = get_cookies(cookies)
    data: Response = requests.get(url, cookies=rCookies)
    return data


def second_request(url, cookies):
    rCookies = get_cookies(cookies)
    data: Response = requests.get(url, cookies=rCookies, allow_redirects=False)
    return data


def third_request(url, cookies, profile):
    payload = {
        "profil": profile
    }
    rCookies = get_cookies(cookies)
    data: Response = requests.get(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def fourth_request(url, cookies):
    rCookies = get_cookies(cookies)
    payload = {
        "wa": "wsignoutcleanup1.0",
        "wreply": url.split("wreply=")[1].split("&")[0],
        "wctx": url.split("&wctx=")[1].replace("%3D", "=")
    }
    data: Response = requests.get(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def fifth_request(url, cookies, profile):
    rCookies = get_cookies(cookies)
    payload = {
        "returnUrl": "/Start?profil=" + profile
    }
    data: Response = requests.get(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def sixth_request(url, cookies, loginEndPoint):
    rCookies = get_cookies(cookies)
    payload = {
        "wa": "wsignin1.0",
        "wtrealm": loginEndPoint,
        "wctx": url.split("&wctx=")[1].replace("%3D", "=").replace("%26", "&")
    }
    data: Response = requests.get(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def seventh_request(url, cookies, wsignin):
    rCookies = get_cookies(cookies)
    payload = {
        "wa": "wsignin1.0",
        "wtrealm": wsignin,
        "wctx": url.split("&wctx=")[1].replace("%3D", "=").replace("%26", "&")
    }
    data: Response = requests.get(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def eighth_request(url, cookies, wresult, wctx):
    rCookies = get_cookies(cookies)
    payload = {
        "wa": "wsignin1.0",
        "wresult": wresult,
        "wctx": wctx
    }
    data: Response = requests.post(url, cookies=rCookies, data=payload, allow_redirects=True)
    return data


def ninth_request(url, cookies, wresult, wctx):
    rCookies = get_cookies(cookies)
    payload = {
        "wa": "wsignin1.0",
        "wresult": wresult,
        "wctx": wctx
    }
    data: Response = requests.post(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def tenth_request(url, cookies, profile):
    rCookies = get_cookies(cookies)
    payload = {
        "profil": profile
    }
    data: Response = requests.post(url, cookies=rCookies, data=payload, allow_redirects=False)
    return data


def last_request(url, cookies):
    rCookies = get_cookies(cookies)
    data: Response = requests.get(url, cookies=rCookies)
    return data