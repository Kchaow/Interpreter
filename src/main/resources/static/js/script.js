window.onload = function () {



    let button = document.querySelector('#run');
    button.addEventListener('click', async function () {
        cleanText();
        let listing = document.querySelector('#input-area').textContent;
        let body = {
            listing: listing
        };
        let url = `/run`;
        let response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(body)
        })
        if (response.ok) {
            let json = await response.json();
            console.log(json);
        } else if (response.status == 400) {
            let json = await response.json();
            console.log(json);
        }
    });


}

function cleanText() {
    let inputArea = document.querySelector('#input-area');
    inputArea.querySelectorAll('.error').forEach(x => {
        let newNode = document.createTextNode(x.textContent);
        inputArea.replaceChild(newNode, x);
    });
}

function markError(line, symbolCount, symbolLength) {
    let text = document.querySelector('#input-area').textContent;
    
}
