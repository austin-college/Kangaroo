<div class="textbook">

    <div class="photo">
        <a href="${textbook.amazonLink}"><img src="${textbook.imageUrl}" height="90px"/></a>
    </div>

    <div style="margin-left: 100px; padding-top: 10px;">
        <div class="title">
            <a href="${textbook.amazonLink}">${textbook.title}</a> <g:if
                test="${textbook.edition}">(${textbook.edition} edition)</g:if>
        </div>


        <div class="price">
            <g:if test="${textbook.matchedOnAmazon && textbook.amazonPrice < textbook.bookstoreNewPrice}">
                <div>
                    <span class="oldPrice">$${textbook.bookstoreNewPrice}</span>
                    <b>$${textbook.amazonPrice}</b>
                </div>
            </g:if>
            <g:else>
                <div><b>$${textbook.bookstoreNewPrice}</b></div>
            </g:else>
        </div>

        <g:if test="${textbook.author}">
            <div class="author">
                by ${textbook.author}
            </div>
        </g:if>
    </div>
</div>