const naverOAuthLoginURL = (function () {
    const url = 'https://nid.naver.com/oauth2.0/authorize';
    const param = {
        response_type: 'code',
        client_id: 'AlS3TCLxJYn7SNPp75LE',
        redirect_uri: 'http://localhost:8080/login/naver',
        state: 'hello',
    };

    function encodeQueryData(data) {
        const ret = [];
        for (let d in data)
            ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
        return ret.join('&');
    }

    return url + '?' + encodeQueryData(param);
})();

function submit() {
    alert('implement the post');
}

window.onload = function () {
    let div = document.getElementById('login');
    let a = document.createElement('a');
    a.href = naverOAuthLoginURL;
    a.append('네이버 아이디로 로그인하기');
    div.append(a);
};
